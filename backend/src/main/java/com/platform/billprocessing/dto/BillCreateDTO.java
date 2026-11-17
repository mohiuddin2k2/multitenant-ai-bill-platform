package com.platform.billprocessing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillCreateDTO {
    @NotBlank(message = "Invoice number is required")
    private String invoiceNumber;

    @NotBlank(message = "Vendor name is required")
    private String vendorName;

    private String vendorTaxId;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Bill date is required")
    private LocalDate billDate;

    private LocalDate dueDate;
    private String currency = "INR";
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    private String notes;
    private String fileUrl;
    private String fileHash;
    private BigDecimal aiConfidenceScore;
    private List<BillItemDTO> items;

    public BillCreateDTO() {
    }

    public BillCreateDTO(String invoiceNumber, String vendorName, String vendorTaxId, String category,
                         LocalDate billDate, LocalDate dueDate, String currency, BigDecimal subtotalAmount,
                         BigDecimal taxAmount, BigDecimal totalAmount, String notes, String fileUrl,
                         String fileHash, BigDecimal aiConfidenceScore, List<BillItemDTO> items) {
        this.invoiceNumber = invoiceNumber;
        this.vendorName = vendorName;
        this.vendorTaxId = vendorTaxId;
        this.category = category;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.currency = currency != null ? currency : "INR";
        this.subtotalAmount = subtotalAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.fileUrl = fileUrl;
        this.fileHash = fileHash;
        this.aiConfidenceScore = aiConfidenceScore;
        this.items = items;
    }

    public static BillCreateDTOBuilder builder() {
        return new BillCreateDTOBuilder();
    }

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
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getFileHash() { return fileHash; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
    public BigDecimal getAiConfidenceScore() { return aiConfidenceScore; }
    public void setAiConfidenceScore(BigDecimal aiConfidenceScore) { this.aiConfidenceScore = aiConfidenceScore; }
    public List<BillItemDTO> getItems() { return items; }
    public void setItems(List<BillItemDTO> items) { this.items = items; }

    public static class BillCreateDTOBuilder {
        private String invoiceNumber;
        private String vendorName;
        private String vendorTaxId;
        private String category;
        private LocalDate billDate;
        private LocalDate dueDate;
        private String currency = "INR";
        private BigDecimal subtotalAmount;
        private BigDecimal taxAmount;
        private BigDecimal totalAmount;
        private String notes;
        private String fileUrl;
        private String fileHash;
        private BigDecimal aiConfidenceScore;
        private List<BillItemDTO> items;

        public BillCreateDTOBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public BillCreateDTOBuilder vendorName(String vendorName) { this.vendorName = vendorName; return this; }
        public BillCreateDTOBuilder vendorTaxId(String vendorTaxId) { this.vendorTaxId = vendorTaxId; return this; }
        public BillCreateDTOBuilder category(String category) { this.category = category; return this; }
        public BillCreateDTOBuilder billDate(LocalDate billDate) { this.billDate = billDate; return this; }
        public BillCreateDTOBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public BillCreateDTOBuilder currency(String currency) { this.currency = currency; return this; }
        public BillCreateDTOBuilder subtotalAmount(BigDecimal subtotalAmount) { this.subtotalAmount = subtotalAmount; return this; }
        public BillCreateDTOBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public BillCreateDTOBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public BillCreateDTOBuilder notes(String notes) { this.notes = notes; return this; }
        public BillCreateDTOBuilder fileUrl(String fileUrl) { this.fileUrl = fileUrl; return this; }
        public BillCreateDTOBuilder fileHash(String fileHash) { this.fileHash = fileHash; return this; }
        public BillCreateDTOBuilder aiConfidenceScore(BigDecimal aiConfidenceScore) { this.aiConfidenceScore = aiConfidenceScore; return this; }
        public BillCreateDTOBuilder items(List<BillItemDTO> items) { this.items = items; return this; }

        public BillCreateDTO build() {
            return new BillCreateDTO(invoiceNumber, vendorName, vendorTaxId, category, billDate,
                    dueDate, currency, subtotalAmount, taxAmount, totalAmount, notes, fileUrl,
                    fileHash, aiConfidenceScore, items);
        }
    }
}
