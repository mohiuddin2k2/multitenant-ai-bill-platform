package com.platform.billprocessing.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CategoryBreakdownDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String category;
    private long billCount;
    private BigDecimal totalAmount;

    public CategoryBreakdownDTO() {
    }

    public CategoryBreakdownDTO(String category, long billCount, BigDecimal totalAmount) {
        this.category = category;
        this.billCount = billCount;
        this.totalAmount = totalAmount;
    }

    public static CategoryBreakdownDTOBuilder builder() {
        return new CategoryBreakdownDTOBuilder();
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public long getBillCount() { return billCount; }
    public void setBillCount(long billCount) { this.billCount = billCount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public static class CategoryBreakdownDTOBuilder {
        private String category;
        private long billCount;
        private BigDecimal totalAmount;

        public CategoryBreakdownDTOBuilder category(String category) { this.category = category; return this; }
        public CategoryBreakdownDTOBuilder billCount(long billCount) { this.billCount = billCount; return this; }
        public CategoryBreakdownDTOBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }

        public CategoryBreakdownDTO build() {
            return new CategoryBreakdownDTO(category, billCount, totalAmount);
        }
    }
}
