package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.AnomalySeverity;
import com.platform.billprocessing.model.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private Long id;
    private Long tenantId;
    private Long submittedByUserId;
    private String submittedByUserName;
    private String invoiceNumber;
    private String vendorName;
    private String vendorTaxId;
    private String category;
    private LocalDate billDate;
    private LocalDate dueDate;
    private String currency;
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BillStatus status;
    private String fileUrl;
    private String fileHash;
    private String notes;
    private String rejectionReason;
    private Boolean isAnomaly;
    private AnomalySeverity anomalySeverity;
    private String anomalyReasons;
    private BigDecimal aiConfidenceScore;
    private List<BillItemDTO> items;
    private List<BillAuditLogDTO> auditLogs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

