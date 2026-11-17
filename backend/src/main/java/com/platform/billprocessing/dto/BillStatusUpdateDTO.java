package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.BillStatus;
import jakarta.validation.constraints.NotNull;

public class BillStatusUpdateDTO {
    @NotNull(message = "Target status is required")
    private BillStatus status;

    private String comment;
    private String rejectionReason;

    public BillStatusUpdateDTO() {
    }

    public BillStatusUpdateDTO(BillStatus status, String comment, String rejectionReason) {
        this.status = status;
        this.comment = comment;
        this.rejectionReason = rejectionReason;
    }

    public static BillStatusUpdateDTOBuilder builder() {
        return new BillStatusUpdateDTOBuilder();
    }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public static class BillStatusUpdateDTOBuilder {
        private BillStatus status;
        private String comment;
        private String rejectionReason;

        public BillStatusUpdateDTOBuilder status(BillStatus status) { this.status = status; return this; }
        public BillStatusUpdateDTOBuilder comment(String comment) { this.comment = comment; return this; }
        public BillStatusUpdateDTOBuilder rejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; return this; }

        public BillStatusUpdateDTO build() {
            return new BillStatusUpdateDTO(status, comment, rejectionReason);
        }
    }
}
