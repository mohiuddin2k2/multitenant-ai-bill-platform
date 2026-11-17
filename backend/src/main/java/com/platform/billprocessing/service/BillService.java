package com.platform.billprocessing.service;

import com.platform.billprocessing.dto.*;
import com.platform.billprocessing.exception.ResourceNotFoundException;
import com.platform.billprocessing.exception.TenantViolationException;
import com.platform.billprocessing.model.*;
import com.platform.billprocessing.repository.BillAuditLogRepository;
import com.platform.billprocessing.repository.BillRepository;
import com.platform.billprocessing.repository.UserRepository;
import com.platform.billprocessing.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BillService {

    private static final Logger log = LoggerFactory.getLogger(BillService.class);

    private final BillRepository billRepository;
    private final BillAuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final AnomalyDetectionService anomalyDetectionService;

    public BillService(
            BillRepository billRepository,
            BillAuditLogRepository auditLogRepository,
            UserRepository userRepository,
            AnomalyDetectionService anomalyDetectionService) {
        this.billRepository = billRepository;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
        this.anomalyDetectionService = anomalyDetectionService;
    }

    @Transactional(readOnly = true)
    public Page<BillDTO> getBills(Long tenantId, BillStatus status, Pageable pageable) {
        Page<Bill> billsPage;
        if (status != null) {
            billsPage = billRepository.findByTenantIdAndStatus(tenantId, status, pageable);
        } else {
            billsPage = billRepository.findByTenantId(tenantId, pageable);
        }
        return billsPage.map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<BillDTO> getBillsForUser(Long tenantId, Long userId, String userRole, BillStatus status, Pageable pageable) {
        if ("CLERK".equals(userRole)) {
            Page<Bill> page = status == null
                    ? billRepository.findByTenantIdAndAssignedToUserId(tenantId, userId, pageable)
                    : billRepository.findByTenantIdAndAssignedToUserIdAndStatus(tenantId, userId, status, pageable);
            return page.map(this::mapToDTO);
        }
        return getBills(tenantId, status, pageable);
    }

    @Transactional(readOnly = true)
    public BillDTO getBillById(Long billId, Long currentTenantId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + billId));

        // Strict Tenant Isolation Verification
        if (!bill.getTenantId().equals(currentTenantId)) {
            throw new TenantViolationException("Security violation: Tenant " + currentTenantId + " is not authorized to access bill " + billId + " belonging to Tenant " + bill.getTenantId());
        }

        return mapToDTO(bill);
    }

    @Transactional
    @CacheEvict(value = "dashboard-summary", key = "#tenantId")
    public BillDTO createBill(BillCreateDTO dto, Long tenantId, Long userId) {
        log.info("Creating new bill for tenant {}: invoice '{}'", tenantId, dto.getInvoiceNumber());

        // Perform anomaly & duplicate detection check
        AnomalyAnalysisDTO anomalyAnalysis = anomalyDetectionService.analyzeBill(
                tenantId,
                dto.getVendorName(),
                dto.getInvoiceNumber(),
                dto.getTotalAmount(),
                dto.getFileHash()
        );

        Bill bill = Bill.builder()
                .tenantId(tenantId)
                .submittedByUserId(userId)
                .invoiceNumber(dto.getInvoiceNumber())
                .vendorName(dto.getVendorName())
                .vendorTaxId(dto.getVendorTaxId())
                .category(dto.getCategory())
                .billDate(dto.getBillDate())
                .dueDate(dto.getDueDate())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "INR")
                .subtotalAmount(dto.getSubtotalAmount() != null ? dto.getSubtotalAmount() : dto.getTotalAmount())
                .taxAmount(dto.getTaxAmount() != null ? dto.getTaxAmount() : BigDecimal.ZERO)
                .totalAmount(dto.getTotalAmount())
                .status(anomalyAnalysis.isDuplicate() ? BillStatus.REJECTED : BillStatus.PENDING)
                .notes(dto.getNotes())
                .fileUrl(dto.getFileUrl())
                .fileHash(dto.getFileHash())
                .isAnomaly(anomalyAnalysis.isAnomaly())
                .anomalySeverity(anomalyAnalysis.getSeverity())
                .anomalyReasons(anomalyAnalysis.getAnomalyReasons() != null ? String.join("; ", anomalyAnalysis.getAnomalyReasons()) : null)
                .aiConfidenceScore(dto.getAiConfidenceScore())
                .rejectionReason(anomalyAnalysis.isDuplicate() ? "Auto-rejected: Duplicate invoice number or identical file hash detected." : null)
                .build();

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (BillItemDTO itemDTO : dto.getItems()) {
                BillItem item = BillItem.builder()
                        .description(itemDTO.getDescription())
                        .quantity(itemDTO.getQuantity() != null ? itemDTO.getQuantity() : BigDecimal.ONE)
                        .unitPrice(itemDTO.getUnitPrice())
                        .totalPrice(itemDTO.getTotalPrice() != null ? itemDTO.getTotalPrice() : itemDTO.getUnitPrice())
                        .build();
                bill.addItem(item);
            }
        }

        bill = billRepository.save(bill);

        // Audit entry
        BillAuditLog logEntry = BillAuditLog.builder()
                .action("CREATED")
                .performedByUserId(userId)
                .comment("Bill submitted via platform API")
                .build();
        bill.addAuditLog(logEntry);
        auditLogRepository.save(logEntry);

        return mapToDTO(bill);
    }

    @Transactional
    @CacheEvict(value = "dashboard-summary", key = "#tenantId")
    public BillDTO updateStatus(Long billId, Long tenantId, Long userId, String userRole, BillStatusUpdateDTO updateDTO) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + billId));

        if (!bill.getTenantId().equals(tenantId)) {
            throw new TenantViolationException("Cross-tenant mutation denied");
        }

        BillStatus previousStatus = bill.getStatus();
        validateWorkflowTransition(bill, previousStatus, updateDTO.getStatus(), userId, userRole);

        if (updateDTO.getStatus() == BillStatus.REJECTED && (updateDTO.getRejectionReason() == null || updateDTO.getRejectionReason().isBlank())) {
            throw new IllegalArgumentException("A rejection reason must be provided when rejecting a bill");
        }

        bill.setStatus(updateDTO.getStatus());
        if (updateDTO.getRejectionReason() != null) {
            bill.setRejectionReason(updateDTO.getRejectionReason());
        }

        bill = billRepository.save(bill);

        // Record audit trail
        BillAuditLog auditLog = BillAuditLog.builder()
                .bill(bill)
                .tenantId(tenantId)
                .action(updateDTO.getStatus().name())
                .performedByUserId(userId)
                .comment(updateDTO.getComment() != null ? updateDTO.getComment() : "Status updated from " + previousStatus + " to " + updateDTO.getStatus())
                .build();
        auditLogRepository.save(auditLog);

        return mapToDTO(bill);
    }

    @Transactional
    @CacheEvict(value = "dashboard-summary", key = "#tenantId")
    public BillDTO assignToClerk(Long billId, Long tenantId, Long actorId, Long clerkUserId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + billId));
        if (!bill.getTenantId().equals(tenantId)) {
            throw new TenantViolationException("Cross-tenant assignment denied");
        }
        User clerk = userRepository.findById(clerkUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Clerk not found with ID: " + clerkUserId));
        if (!clerk.getTenantId().equals(tenantId) || clerk.getRole() != Role.CLERK || !Boolean.TRUE.equals(clerk.getEnabled())) {
            throw new IllegalArgumentException("Choose an enabled clerk in the active organization");
        }
        if (bill.getStatus() != BillStatus.PENDING && bill.getStatus() != BillStatus.IN_REVIEW
                && bill.getStatus() != BillStatus.ASSIGNED_TO_CLERK) {
            throw new IllegalArgumentException("Only unassigned or clerk-assigned bills can be assigned to a clerk");
        }

        bill.setAssignedToUserId(clerkUserId);
        bill.setStatus(BillStatus.ASSIGNED_TO_CLERK);
        bill = billRepository.save(bill);
        saveWorkflowAudit(bill, tenantId, actorId, "ASSIGNED_TO_CLERK", "Bill assigned to " + clerk.getFullName());
        return mapToDTO(bill);
    }

    @Transactional
    @CacheEvict(value = "dashboard-summary", key = "#tenantId")
    public BillDTO updateAssignedBillDetails(Long billId, Long tenantId, Long userId, BillCreateDTO dto) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with ID: " + billId));
        if (!bill.getTenantId().equals(tenantId)) {
            throw new TenantViolationException("Cross-tenant mutation denied");
        }
        if (bill.getStatus() != BillStatus.ASSIGNED_TO_CLERK || !userId.equals(bill.getAssignedToUserId())) {
            throw new AccessDeniedException("Only the assigned clerk can edit bill details before review");
        }

        bill.setInvoiceNumber(dto.getInvoiceNumber());
        bill.setVendorName(dto.getVendorName());
        bill.setVendorTaxId(dto.getVendorTaxId());
        bill.setCategory(dto.getCategory());
        bill.setBillDate(dto.getBillDate());
        bill.setDueDate(dto.getDueDate());
        bill.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "INR");
        bill.setSubtotalAmount(dto.getSubtotalAmount() != null ? dto.getSubtotalAmount() : dto.getTotalAmount());
        bill.setTaxAmount(dto.getTaxAmount() != null ? dto.getTaxAmount() : BigDecimal.ZERO);
        bill.setTotalAmount(dto.getTotalAmount());
        bill.setNotes(dto.getNotes());
        bill.setFileUrl(dto.getFileUrl());
        bill.setFileHash(dto.getFileHash());
        bill = billRepository.save(bill);

        saveWorkflowAudit(bill, tenantId, userId, "CLERK_DETAILS_UPDATED", "Assigned clerk reviewed and updated bill details");
        return mapToDTO(bill);
    }

    private void validateWorkflowTransition(Bill bill, BillStatus current, BillStatus target, Long userId, String role) {
        boolean allowed = false;
        if (target == BillStatus.REJECTED) {
            allowed = ("ASSISTANT_OFFICER".equals(role) && current == BillStatus.PENDING_ASSISTANT_REVIEW)
                    || ("HIGHER_OFFICER".equals(role) && current == BillStatus.PENDING_HIGHER_REVIEW)
                    || ("APPROVER".equals(role) && (current == BillStatus.PENDING || current == BillStatus.IN_REVIEW));
        } else if ("CLERK".equals(role)) {
            allowed = current == BillStatus.ASSIGNED_TO_CLERK && target == BillStatus.PENDING_ASSISTANT_REVIEW
                    && userId.equals(bill.getAssignedToUserId());
        } else if ("ASSISTANT_OFFICER".equals(role)) {
            allowed = current == BillStatus.PENDING_ASSISTANT_REVIEW && target == BillStatus.PENDING_HIGHER_REVIEW;
        } else if ("HIGHER_OFFICER".equals(role)) {
            allowed = current == BillStatus.PENDING_HIGHER_REVIEW && target == BillStatus.APPROVED;
        } else if ("ADMIN".equals(role)) {
            allowed = current == BillStatus.APPROVED && target == BillStatus.PAID;
        } else if ("APPROVER".equals(role)) {
            // Keep the original demo approval path working for legacy accounts.
            allowed = (current == BillStatus.PENDING || current == BillStatus.IN_REVIEW)
                    && (target == BillStatus.APPROVED || target == BillStatus.REJECTED);
        }
        if (!allowed) {
            throw new AccessDeniedException("This role cannot move the bill from " + current + " to " + target);
        }
    }

    private void saveWorkflowAudit(Bill bill, Long tenantId, Long actorId, String action, String comment) {
        BillAuditLog audit = BillAuditLog.builder().bill(bill).tenantId(tenantId)
                .action(action).performedByUserId(actorId).comment(comment).build();
        auditLogRepository.save(audit);
    }

    public BillDTO mapToDTO(Bill bill) {
        String submitterName = null;
        if (bill.getSubmittedByUserId() != null) {
            submitterName = userRepository.findById(bill.getSubmittedByUserId())
                    .map(User::getFullName)
                    .orElse("System");
        }

        List<BillItemDTO> itemDTOs = bill.getItems() != null ?
                bill.getItems().stream().map(i -> BillItemDTO.builder()
                        .id(i.getId())
                        .description(i.getDescription())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .totalPrice(i.getTotalPrice())
                        .build()).collect(Collectors.toList()) : List.of();

        List<BillAuditLogDTO> auditDTOs = bill.getAuditLogs() != null ?
                bill.getAuditLogs().stream().map(a -> BillAuditLogDTO.builder()
                        .id(a.getId())
                        .action(a.getAction())
                        .performedByUserId(a.getPerformedByUserId())
                        .comment(a.getComment())
                        .timestamp(a.getTimestamp())
                        .build()).collect(Collectors.toList()) : List.of();

        return BillDTO.builder()
                .id(bill.getId())
                .tenantId(bill.getTenantId())
                .submittedByUserId(bill.getSubmittedByUserId())
                .assignedToUserId(bill.getAssignedToUserId())
                .submittedByUserName(submitterName)
                .invoiceNumber(bill.getInvoiceNumber())
                .vendorName(bill.getVendorName())
                .vendorTaxId(bill.getVendorTaxId())
                .category(bill.getCategory())
                .billDate(bill.getBillDate())
                .dueDate(bill.getDueDate())
                .currency(bill.getCurrency())
                .subtotalAmount(bill.getSubtotalAmount())
                .taxAmount(bill.getTaxAmount())
                .totalAmount(bill.getTotalAmount())
                .status(bill.getStatus())
                .fileUrl(bill.getFileUrl())
                .fileHash(bill.getFileHash())
                .notes(bill.getNotes())
                .rejectionReason(bill.getRejectionReason())
                .isAnomaly(bill.getIsAnomaly())
                .anomalySeverity(bill.getAnomalySeverity())
                .anomalyReasons(bill.getAnomalyReasons())
                .aiConfidenceScore(bill.getAiConfidenceScore())
                .items(itemDTOs)
                .auditLogs(auditDTOs)
                .createdAt(bill.getCreatedAt())
                .updatedAt(bill.getUpdatedAt())
                .build();
    }
}

