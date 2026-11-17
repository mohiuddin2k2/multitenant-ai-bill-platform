package com.platform.billprocessing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BillItemDTO {
    private Long id;

    @NotBlank(message = "Item description is required")
    private String description;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.00", message = "Unit price must be non-negative")
    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    public BillItemDTO() {
    }

    public BillItemDTO(Long id, String description, BigDecimal quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public static BillItemDTOBuilder builder() {
        return new BillItemDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public static class BillItemDTOBuilder {
        private Long id;
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;

        public BillItemDTOBuilder id(Long id) { this.id = id; return this; }
        public BillItemDTOBuilder description(String description) { this.description = description; return this; }
        public BillItemDTOBuilder quantity(BigDecimal quantity) { this.quantity = quantity; return this; }
        public BillItemDTOBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public BillItemDTOBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }

        public BillItemDTO build() {
            return new BillItemDTO(id, description, quantity, unitPrice, totalPrice);
        }
    }
}
