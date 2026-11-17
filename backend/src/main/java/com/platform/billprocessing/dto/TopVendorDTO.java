package com.platform.billprocessing.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TopVendorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vendorName;
    private long billCount;
    private BigDecimal totalAmount;

    public TopVendorDTO() {
    }

    public TopVendorDTO(String vendorName, long billCount, BigDecimal totalAmount) {
        this.vendorName = vendorName;
        this.billCount = billCount;
        this.totalAmount = totalAmount;
    }

    public static TopVendorDTOBuilder builder() {
        return new TopVendorDTOBuilder();
    }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public long getBillCount() { return billCount; }
    public void setBillCount(long billCount) { this.billCount = billCount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public static class TopVendorDTOBuilder {
        private String vendorName;
        private long billCount;
        private BigDecimal totalAmount;

        public TopVendorDTOBuilder vendorName(String vendorName) { this.vendorName = vendorName; return this; }
        public TopVendorDTOBuilder billCount(long billCount) { this.billCount = billCount; return this; }
        public TopVendorDTOBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }

        public TopVendorDTO build() {
            return new TopVendorDTO(vendorName, billCount, totalAmount);
        }
    }
}
