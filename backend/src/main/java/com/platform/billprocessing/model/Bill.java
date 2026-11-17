package com.platform.billprocessing.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "submitted_by_user_id")
    private Long submittedByUserId;

    @Column(name = "assigned_to_user_id")
    private Long assignedToUserId;

    @Column(name = "invoice_number", nullable = false, length = 100)
    private String invoiceNumber;

    @Column(name = "vendor_name", nullable = false, length = 150)
    private String vendorName;

    @Column(name = "vendor_tax_id", length = 50)
    private String vendorTaxId;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(nullable = false, length = 10)
    private String currency = "INR";

    @Column(name = "subtotal_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotalAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private BillStatus status = BillStatus.PENDING;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "file_hash", length = 128)
    private String fileHash;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "is_anomaly", nullable = false)
    private Boolean isAnomaly = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "anomaly_severity", length = 20)
    private AnomalySeverity anomalySeverity;

    @Column(name = "anomaly_reasons", columnDefinition = "TEXT")
    private String anomalyReasons;

    @Column(name = "ai_confidence_score", precision = 5, scale = 2)
    private BigDecimal aiConfidenceScore;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillAuditLog> auditLogs = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Bill() {
    }

    public Bill(Long id, Long tenantId, Long submittedByUserId, String invoiceNumber, String vendorName,
                String vendorTaxId, String category, LocalDate billDate, LocalDate dueDate, String currency,
                BigDecimal subtotalAmount, BigDecimal taxAmount, BigDecimal totalAmount, BillStatus status,
                String fileUrl, String fileHash, String notes, String rejectionReason, Boolean isAnomaly,
                AnomalySeverity anomalySeverity, String anomalyReasons, BigDecimal aiConfidenceScore,
                List<BillItem> items, List<BillAuditLog> auditLogs, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.submittedByUserId = submittedByUserId;
        this.invoiceNumber = invoiceNumber;
        this.vendorName = vendorName;
        this.vendorTaxId = vendorTaxId;
        this.category = category;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.currency = currency != null ? currency : "INR";
        this.subtotalAmount = subtotalAmount != null ? subtotalAmount : BigDecimal.ZERO;
        this.taxAmount = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        this.totalAmount = totalAmount;
        this.status = status != null ? status : BillStatus.PENDING;
        this.fileUrl = fileUrl;
        this.fileHash = fileHash;
        this.notes = notes;
        this.rejectionReason = rejectionReason;
        this.isAnomaly = isAnomaly != null ? isAnomaly : false;
        this.anomalySeverity = anomalySeverity;
        this.anomalyReasons = anomalyReasons;
        this.aiConfidenceScore = aiConfidenceScore;
        this.items = items != null ? items : new ArrayList<>();
        this.auditLogs = auditLogs != null ? auditLogs : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BillBuilder builder() {
        return new BillBuilder();
    }

    public void addItem(BillItem item) {
        items.add(item);
        item.setBill(this);
        item.setTenantId(this.tenantId);
    }

    public void addAuditLog(BillAuditLog log) {
        auditLogs.add(log);
        log.setBill(this);
        log.setTenantId(this.tenantId);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public Long getSubmittedByUserId() { return submittedByUserId; }
    public void setSubmittedByUserId(Long submittedByUserId) { this.submittedByUserId = submittedByUserId; }
    public Long getAssignedToUserId() { return assignedToUserId; }
    public void setAssignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; }
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
    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }
    public List<BillAuditLog> getAuditLogs() { return auditLogs; }
    public void setAuditLogs(List<BillAuditLog> auditLogs) { this.auditLogs = auditLogs; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class BillBuilder {
        private Long id;
        private Long tenantId;
        private Long submittedByUserId;
        private Long assignedToUserId;
        private String invoiceNumber;
        private String vendorName;
        private String vendorTaxId;
        private String category;
        private LocalDate billDate;
        private LocalDate dueDate;
        private String currency = "INR";
        private BigDecimal subtotalAmount = BigDecimal.ZERO;
        private BigDecimal taxAmount = BigDecimal.ZERO;
        private BigDecimal totalAmount;
        private BillStatus status = BillStatus.PENDING;
        private String fileUrl;
        private String fileHash;
        private String notes;
        private String rejectionReason;
        private Boolean isAnomaly = false;
        private AnomalySeverity anomalySeverity;
        private String anomalyReasons;
        private BigDecimal aiConfidenceScore;
        private List<BillItem> items = new ArrayList<>();
        private List<BillAuditLog> auditLogs = new ArrayList<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public BillBuilder id(Long id) { this.id = id; return this; }
        public BillBuilder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public BillBuilder submittedByUserId(Long submittedByUserId) { this.submittedByUserId = submittedByUserId; return this; }
        public BillBuilder assignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; return this; }
        public BillBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public BillBuilder vendorName(String vendorName) { this.vendorName = vendorName; return this; }
        public BillBuilder vendorTaxId(String vendorTaxId) { this.vendorTaxId = vendorTaxId; return this; }
        public BillBuilder category(String category) { this.category = category; return this; }
        public BillBuilder billDate(LocalDate billDate) { this.billDate = billDate; return this; }
        public BillBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public BillBuilder currency(String currency) { this.currency = currency; return this; }
        public BillBuilder subtotalAmount(BigDecimal subtotalAmount) { this.subtotalAmount = subtotalAmount; return this; }
        public BillBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public BillBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public BillBuilder status(BillStatus status) { this.status = status; return this; }
        public BillBuilder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public BillBuilder fileHash(String fileHash) { this.fileHash = fileHash; return this; }
        public BillBuilder notes(String notes) { this.notes = notes; return this; }
        public BillBuilder rejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; return this; }
        public BillBuilder isAnomaly(Boolean isAnomaly) { this.isAnomaly = isAnomaly; return this; }
        public BillBuilder anomalySeverity(AnomalySeverity anomalySeverity) { this.anomalySeverity = anomalySeverity; return this; }
        public BillBuilder anomalyReasons(String anomalyReasons) { this.anomalyReasons = anomalyReasons; return this; }
        public BillBuilder aiConfidenceScore(BigDecimal aiConfidenceScore) { this.aiConfidenceScore = aiConfidenceScore; return this; }
        public BillBuilder items(List<BillItem> items) { this.items = items; return this; }
        public BillBuilder auditLogs(List<BillAuditLog> auditLogs) { this.auditLogs = auditLogs; return this; }
        public BillBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BillBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Bill build() {
            Bill bill = new Bill(id, tenantId, submittedByUserId, invoiceNumber, vendorName, vendorTaxId,
                    category, billDate, dueDate, currency, subtotalAmount, taxAmount, totalAmount,
                    status, fileUrl, fileHash, notes, rejectionReason, isAnomaly, anomalySeverity,
                    anomalyReasons, aiConfidenceScore, items, auditLogs, createdAt, updatedAt);
            bill.setAssignedToUserId(assignedToUserId);
            return bill;
        }
    }
}
