package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FilterCriteriaDTO {
    private String vendorName;
    private String category;
    private BillStatus status;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isAnomalyOnly;

    public FilterCriteriaDTO() {
    }

    public FilterCriteriaDTO(String vendorName, String category, BillStatus status, BigDecimal minAmount,
                             BigDecimal maxAmount, LocalDate startDate, LocalDate endDate, Boolean isAnomalyOnly) {
        this.vendorName = vendorName;
        this.category = category;
        this.status = status;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAnomalyOnly = isAnomalyOnly;
    }

    public static FilterCriteriaDTOBuilder builder() {
        return new FilterCriteriaDTOBuilder();
    }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
    public BigDecimal getMinAmount() { return minAmount; }
    public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Boolean getIsAnomalyOnly() { return isAnomalyOnly; }
    public void setIsAnomalyOnly(Boolean isAnomalyOnly) { this.isAnomalyOnly = isAnomalyOnly; }

    public static class FilterCriteriaDTOBuilder {
        private String vendorName;
        private String category;
        private BillStatus status;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean isAnomalyOnly;

        public FilterCriteriaDTOBuilder vendorName(String vendorName) { this.vendorName = vendorName; return this; }
        public FilterCriteriaDTOBuilder category(String category) { this.category = category; return this; }
        public FilterCriteriaDTOBuilder status(BillStatus status) { this.status = status; return this; }
        public FilterCriteriaDTOBuilder minAmount(BigDecimal minAmount) { this.minAmount = minAmount; return this; }
        public FilterCriteriaDTOBuilder maxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; return this; }
        public FilterCriteriaDTOBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public FilterCriteriaDTOBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public FilterCriteriaDTOBuilder isAnomalyOnly(Boolean isAnomalyOnly) { this.isAnomalyOnly = isAnomalyOnly; return this; }

        public FilterCriteriaDTO build() {
            return new FilterCriteriaDTO(vendorName, category, status, minAmount, maxAmount, startDate, endDate, isAnomalyOnly);
        }
    }
}
