package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.AnomalySeverity;
import com.platform.billprocessing.model.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BillDTO {
    private Long id;
    private Long tenantId;
    private Long submittedByUserId;
    private Long assignedToUserId;
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

    public BillDTO() {
    }

    public BillDTO(Long id, Long tenantId, Long submittedByUserId, String submittedByUserName,
                   String invoiceNumber, String vendorName, String vendorTaxId, String category,
                   LocalDate billDate, LocalDate dueDate, String currency, BigDecimal subtotalAmount,
                   BigDecimal taxAmount, BigDecimal totalAmount, BillStatus status, String fileUrl,
                   String fileHash, String notes, String rejectionReason, Boolean isAnomaly,
                   AnomalySeverity anomalySeverity, String anomalyReasons, BigDecimal aiConfidenceScore,
                   List<BillItemDTO> items, List<BillAuditLogDTO> auditLogs, LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.submittedByUserId = submittedByUserId;
        this.submittedByUserName = submittedByUserName;
        this.invoiceNumber = invoiceNumber;
        this.vendorName = vendorName;
        this.vendorTaxId = vendorTaxId;
        this.category = category;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.currency = currency;
        this.subtotalAmount = subtotalAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.fileUrl = fileUrl;
        this.fileHash = fileHash;
        this.notes = notes;
        this.rejectionReason = rejectionReason;
        this.isAnomaly = isAnomaly;
        this.anomalySeverity = anomalySeverity;
        this.anomalyReasons = anomalyReasons;
        this.aiConfidenceScore = aiConfidenceScore;
        this.items = items;
        this.auditLogs = auditLogs;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BillDTOBuilder builder() {
        return new BillDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public Long getSubmittedByUserId() { return submittedByUserId; }
    public void setSubmittedByUserId(Long submittedByUserId) { this.submittedByUserId = submittedByUserId; }
    public Long getAssignedToUserId() { return assignedToUserId; }
    public void setAssignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; }
    public String getSubmittedByUserName() { return submittedByUserName; }
    public void setSubmittedByUserName(String submittedByUserName) { this.submittedByUserName = submittedByUserName; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getVendorTaxId() { return vendorTaxId; }
    public void setVendorTaxId(String vendorTaxId) { this.vendorTaxId = vendorTaxId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getSubtotalAmount() { return subtotalAmount; }
    public void setSubtotalAmount(BigDecimal subtotalAmount) { this.subtotalAmount = subtotalAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getFileHash() { return fileHash; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public Boolean getIsAnomaly() { return isAnomaly; }
    public void setIsAnomaly(Boolean isAnomaly) { this.isAnomaly = isAnomaly; }
    public AnomalySeverity getAnomalySeverity() { return anomalySeverity; }
    public void setAnomalySeverity(AnomalySeverity anomalySeverity) { this.anomalySeverity = anomalySeverity; }
    public String getAnomalyReasons() { return anomalyReasons; }
    public void setAnomalyReasons(String anomalyReasons) { this.anomalyReasons = anomalyReasons; }
    public BigDecimal getAiConfidenceScore() { return aiConfidenceScore; }
    public void setAiConfidenceScore(BigDecimal aiConfidenceScore) { this.aiConfidenceScore = aiConfidenceScore; }
    public List<BillItemDTO> getItems() { return items; }
    public void setItems(List<BillItemDTO> items) { this.items = items; }
    public List<BillAuditLogDTO> getAuditLogs() { return auditLogs; }
    public void setAuditLogs(List<BillAuditLogDTO> auditLogs) { this.auditLogs = auditLogs; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class BillDTOBuilder {
        private Long id;
        private Long tenantId;
        private Long submittedByUserId;
        private Long assignedToUserId;
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

        public BillDTOBuilder id(Long id) { this.id = id; return this; }
        public BillDTOBuilder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public BillDTOBuilder submittedByUserId(Long submittedByUserId) { this.submittedByUserId = submittedByUserId; return this; }
        public BillDTOBuilder assignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; return this; }
        public BillDTOBuilder submittedByUserName(String submittedByUserName) { this.submittedByUserName = submittedByUserName; return this; }
        public BillDTOBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public BillDTOBuilder vendorName(String vendorName) { this.vendorName = vendorName; return this; }
        public BillDTOBuilder vendorTaxId(String vendorTaxId) { this.vendorTaxId = vendorTaxId; return this; }
        public BillDTOBuilder category(String category) { this.category = category; return this; }
        public BillDTOBuilder billDate(LocalDate billDate) { this.billDate = billDate; return this; }
        public BillDTOBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public BillDTOBuilder currency(String currency) { this.currency = currency; return this; }
        public BillDTOBuilder subtotalAmount(BigDecimal subtotalAmount) { this.subtotalAmount = subtotalAmount; return this; }
        public BillDTOBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public BillDTOBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public BillDTOBuilder status(BillStatus status) { this.status = status; return this; }
        public BillDTOBuilder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public BillDTOBuilder fileHash(String fileHash) { this.fileHash = fileHash; return this; }
        public BillDTOBuilder notes(String notes) { this.notes = notes; return this; }
        public BillDTOBuilder rejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; return this; }
        public BillDTOBuilder isAnomaly(Boolean isAnomaly) { this.isAnomaly = isAnomaly; return this; }
        public BillDTOBuilder anomalySeverity(AnomalySeverity anomalySeverity) { this.anomalySeverity = anomalySeverity; return this; }
        public BillDTOBuilder anomalyReasons(String anomalyReasons) { this.anomalyReasons = anomalyReasons; return this; }
        public BillDTOBuilder aiConfidenceScore(BigDecimal aiConfidenceScore) { this.aiConfidenceScore = aiConfidenceScore; return this; }
        public BillDTOBuilder items(List<BillItemDTO> items) { this.items = items; return this; }
        public BillDTOBuilder auditLogs(List<BillAuditLogDTO> auditLogs) { this.auditLogs = auditLogs; return this; }
        public BillDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BillDTOBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public BillDTO build() {
            BillDTO dto = new BillDTO(id, tenantId, submittedByUserId, submittedByUserName, invoiceNumber,
                    vendorName, vendorTaxId, category, billDate, dueDate, currency, subtotalAmount,
                    taxAmount, totalAmount, status, fileUrl, fileHash, notes, rejectionReason,
                    isAnomaly, anomalySeverity, anomalyReasons, aiConfidenceScore, items, auditLogs,
                    createdAt, updatedAt);
            dto.setAssignedToUserId(assignedToUserId);
            return dto;
        }
    }
}
