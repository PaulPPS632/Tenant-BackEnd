package com.superfact.inventory.Tenant;

public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }
    public static String getCurrentTenant() {
        return currentTenant.get();
    }
    public static void clearCurrentTenant() {
        currentTenant.remove();
    }
}
