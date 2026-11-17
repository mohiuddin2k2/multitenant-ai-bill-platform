package com.platform.billprocessing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bill_items")
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonIgnore
    private Bill bill;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity = BigDecimal.ONE;

    @Column(name = "unit_price", nullable = false, precision = 14, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalPrice;

    public BillItem() {
    }

    public BillItem(Long id, Bill bill, Long tenantId, String description, BigDecimal quantity,
                    BigDecimal unitPrice, BigDecimal totalPrice) {
        this.id = id;
        this.bill = bill;
        this.tenantId = tenantId;
        this.description = description;
        this.quantity = quantity != null ? quantity : BigDecimal.ONE;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public static BillItemBuilder builder() {
        return new BillItemBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public static class BillItemBuilder {
        private Long id;
        private Bill bill;
        private Long tenantId;
        private String description;
        private BigDecimal quantity = BigDecimal.ONE;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;

        public BillItemBuilder id(Long id) { this.id = id; return this; }
        public BillItemBuilder bill(Bill bill) { this.bill = bill; return this; }
        public BillItemBuilder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public BillItemBuilder description(String description) { this.description = description; return this; }
        public BillItemBuilder quantity(BigDecimal quantity) { this.quantity = quantity; return this; }
        public BillItemBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public BillItemBuilder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }

        public BillItem build() {
            return new BillItem(id, bill, tenantId, description, quantity, unitPrice, totalPrice);
        }
    }
}
