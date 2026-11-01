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

@Service
@RequiredArgsConstructor
@Slf4j
public class BillService {

    private final BillRepository billRepository;
    private final BillAuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final AnomalyDetectionService anomalyDetectionService;

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

        // RBAC validation: Approvals & Rejections require APPROVER or ADMIN role
        if ((updateDTO.getStatus() == BillStatus.APPROVED || updateDTO.getStatus() == BillStatus.REJECTED) &&
            !("APPROVER".equalsIgnoreCase(userRole) || "ADMIN".equalsIgnoreCase(userRole))) {
            throw new AccessDeniedException("Only users with role APPROVER or ADMIN are authorized to approve or reject bills");
        }

        if (updateDTO.getStatus() == BillStatus.REJECTED && (updateDTO.getRejectionReason() == null || updateDTO.getRejectionReason().isBlank())) {
            throw new IllegalArgumentException("A rejection reason must be provided when rejecting a bill");
        }

        BillStatus previousStatus = bill.getStatus();
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

