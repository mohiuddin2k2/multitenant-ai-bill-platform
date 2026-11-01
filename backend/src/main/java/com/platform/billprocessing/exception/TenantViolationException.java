package com.platform.billprocessing.exception;

public class TenantViolationException extends RuntimeException {
    public TenantViolationException(String message) {
        super(message);
    }
}

