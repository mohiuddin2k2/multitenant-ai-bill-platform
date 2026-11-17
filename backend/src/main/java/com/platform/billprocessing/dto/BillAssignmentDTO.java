package com.platform.billprocessing.dto;

import jakarta.validation.constraints.NotNull;

public class BillAssignmentDTO {
    @NotNull(message = "Clerk user ID is required")
    private Long clerkUserId;

    public Long getClerkUserId() { return clerkUserId; }
    public void setClerkUserId(Long clerkUserId) { this.clerkUserId = clerkUserId; }
}
