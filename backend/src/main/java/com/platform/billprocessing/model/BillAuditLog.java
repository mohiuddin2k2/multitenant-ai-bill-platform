package com.platform.billprocessing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill_audit_logs")
public class BillAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonIgnore
    private Bill bill;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(name = "performed_by_user_id")
    private Long performedByUserId;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    public BillAuditLog() {
    }

    public BillAuditLog(Long id, Bill bill, Long tenantId, String action, Long performedByUserId,
                        String comment, LocalDateTime timestamp) {
        this.id = id;
        this.bill = bill;
        this.tenantId = tenantId;
        this.action = action;
        this.performedByUserId = performedByUserId;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public static BillAuditLogBuilder builder() {
        return new BillAuditLogBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Long getPerformedByUserId() { return performedByUserId; }
    public void setPerformedByUserId(Long performedByUserId) { this.performedByUserId = performedByUserId; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class BillAuditLogBuilder {
        private Long id;
        private Bill bill;
        private Long tenantId;
        private String action;
        private Long performedByUserId;
        private String comment;
        private LocalDateTime timestamp;

        public BillAuditLogBuilder id(Long id) { this.id = id; return this; }
        public BillAuditLogBuilder bill(Bill bill) { this.bill = bill; return this; }
        public BillAuditLogBuilder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public BillAuditLogBuilder action(String action) { this.action = action; return this; }
        public BillAuditLogBuilder performedByUserId(Long performedByUserId) { this.performedByUserId = performedByUserId; return this; }
        public BillAuditLogBuilder comment(String comment) { this.comment = comment; return this; }
        public BillAuditLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public BillAuditLog build() {
            return new BillAuditLog(id, bill, tenantId, action, performedByUserId, comment, timestamp);
        }
    }
}
