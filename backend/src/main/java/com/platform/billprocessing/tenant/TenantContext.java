package com.platform.billprocessing.tenant;

import org.slf4j.MDC;

public class TenantContext {

    private static final String TENANT_KEY = "tenantId";
    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_ORG_PATH = new ThreadLocal<>();
    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_USER_ROLE = new ThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
        if (tenantId != null) {
            MDC.put(TENANT_KEY, String.valueOf(tenantId));
        } else {
            MDC.remove(TENANT_KEY);
        }
    }

    public static Long getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void setOrgPath(String orgPath) {
        CURRENT_ORG_PATH.set(orgPath);
    }

    public static String getOrgPath() {
        return CURRENT_ORG_PATH.get();
    }

    public static void setUserId(Long userId) {
        CURRENT_USER_ID.set(userId);
    }

    public static Long getUserId() {
        return CURRENT_USER_ID.get();
    }

    public static void setUserRole(String role) {
        CURRENT_USER_ROLE.set(role);
    }

    public static String getUserRole() {
        return CURRENT_USER_ROLE.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_ORG_PATH.remove();
        CURRENT_USER_ID.remove();
        CURRENT_USER_ROLE.remove();
        MDC.remove(TENANT_KEY);
    }
}

