package com.platform.billprocessing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExtractedBillDataDTO {
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
    private BigDecimal confidenceScore;
    private List<BillItemDTO> items;

    public ExtractedBillDataDTO() {
    }

    public ExtractedBillDataDTO(String invoiceNumber, String vendorName, String vendorTaxId, String category,
                                LocalDate billDate, LocalDate dueDate, String currency, BigDecimal subtotalAmount,
                                BigDecimal taxAmount, BigDecimal totalAmount, BigDecimal confidenceScore,
                                List<BillItemDTO> items) {
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
        this.confidenceScore = confidenceScore;
        this.items = items;
    }

    public static ExtractedBillDataDTOBuilder builder() {
        return new ExtractedBillDataDTOBuilder();
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
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    public List<BillItemDTO> getItems() { return items; }
    public void setItems(List<BillItemDTO> items) { this.items = items; }

    public static class ExtractedBillDataDTOBuilder {
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
        private BigDecimal confidenceScore;
        private List<BillItemDTO> items;

        public ExtractedBillDataDTOBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public ExtractedBillDataDTOBuilder vendorName(String vendorName) { this.vendorName = vendorName; return this; }
        public ExtractedBillDataDTOBuilder vendorTaxId(String vendorTaxId) { this.vendorTaxId = vendorTaxId; return this; }
        public ExtractedBillDataDTOBuilder category(String category) { this.category = category; return this; }
        public ExtractedBillDataDTOBuilder billDate(LocalDate billDate) { this.billDate = billDate; return this; }
        public ExtractedBillDataDTOBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public ExtractedBillDataDTOBuilder currency(String currency) { this.currency = currency; return this; }
        public ExtractedBillDataDTOBuilder subtotalAmount(BigDecimal subtotalAmount) { this.subtotalAmount = subtotalAmount; return this; }
        public ExtractedBillDataDTOBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public ExtractedBillDataDTOBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public ExtractedBillDataDTOBuilder confidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public ExtractedBillDataDTOBuilder items(List<BillItemDTO> items) { this.items = items; return this; }

        public ExtractedBillDataDTO build() {
            return new ExtractedBillDataDTO(invoiceNumber, vendorName, vendorTaxId, category,
                    billDate, dueDate, currency, subtotalAmount, taxAmount, totalAmount, confidenceScore, items);
        }
    }
}
