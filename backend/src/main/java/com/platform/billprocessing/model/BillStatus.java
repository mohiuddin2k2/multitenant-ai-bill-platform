package com.platform.billprocessing.model;

public enum BillStatus {
    DRAFT,
    PENDING,
    ASSIGNED_TO_CLERK,
    IN_REVIEW,
    PENDING_ASSISTANT_REVIEW,
    PENDING_HIGHER_REVIEW,
    APPROVED,
    REJECTED,
    PAID
}

