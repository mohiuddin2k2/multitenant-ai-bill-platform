package com.platform.billprocessing.dto;

import java.time.LocalDateTime;

public class BillAuditLogDTO {
    private Long id;
    private String action;
    private Long performedByUserId;
    private String performedByUserName;
    private String comment;
    private LocalDateTime timestamp;

    public BillAuditLogDTO() {
    }

    public BillAuditLogDTO(Long id, String action, Long performedByUserId, String performedByUserName,
                           String comment, LocalDateTime timestamp) {
        this.id = id;
        this.action = action;
        this.performedByUserId = performedByUserId;
        this.performedByUserName = performedByUserName;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public static BillAuditLogDTOBuilder builder() {
        return new BillAuditLogDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Long getPerformedByUserId() { return performedByUserId; }
    public void setPerformedByUserId(Long performedByUserId) { this.performedByUserId = performedByUserId; }
    public String getPerformedByUserName() { return performedByUserName; }
    public void setPerformedByUserName(String performedByUserName) { this.performedByUserName = performedByUserName; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class BillAuditLogDTOBuilder {
        private Long id;
        private String action;
        private Long performedByUserId;
        private String performedByUserName;
        private String comment;
        private LocalDateTime timestamp;

        public BillAuditLogDTOBuilder id(Long id) { this.id = id; return this; }
        public BillAuditLogDTOBuilder action(String action) { this.action = action; return this; }
        public BillAuditLogDTOBuilder performedByUserId(Long performedByUserId) { this.performedByUserId = performedByUserId; return this; }
        public BillAuditLogDTOBuilder performedByUserName(String performedByUserName) { this.performedByUserName = performedByUserName; return this; }
        public BillAuditLogDTOBuilder comment(String comment) { this.comment = comment; return this; }
        public BillAuditLogDTOBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public BillAuditLogDTO build() {
            return new BillAuditLogDTO(id, action, performedByUserId, performedByUserName, comment, timestamp);
        }
    }
}
