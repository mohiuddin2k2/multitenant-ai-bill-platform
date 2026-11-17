package com.platform.billprocessing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TenantQuotaDTO {
    private Long id;
    private Long tenantId;
    private Long monthlyTokenLimit;
    private Long tokensUsedThisMonth;
    private BigDecimal monthlyCostCapUsd;
    private BigDecimal costAccumulatedUsd;
    private LocalDate lastResetDate;
    private Boolean isThrottled;
    private double usagePercent;

    public TenantQuotaDTO() {
    }

    public TenantQuotaDTO(Long id, Long tenantId, Long monthlyTokenLimit, Long tokensUsedThisMonth,
                          BigDecimal monthlyCostCapUsd, BigDecimal costAccumulatedUsd,
                          LocalDate lastResetDate, Boolean isThrottled, double usagePercent) {
        this.id = id;
        this.tenantId = tenantId;
        this.monthlyTokenLimit = monthlyTokenLimit;
        this.tokensUsedThisMonth = tokensUsedThisMonth;
        this.monthlyCostCapUsd = monthlyCostCapUsd;
        this.costAccumulatedUsd = costAccumulatedUsd;
        this.lastResetDate = lastResetDate;
        this.isThrottled = isThrottled;
        this.usagePercent = usagePercent;
    }

    public static TenantQuotaDTOBuilder builder() {
        return new TenantQuotaDTOBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getMonthlyTokenLimit() {
        return monthlyTokenLimit;
    }

    public void setMonthlyTokenLimit(Long monthlyTokenLimit) {
        this.monthlyTokenLimit = monthlyTokenLimit;
    }

    public Long getTokensUsedThisMonth() {
        return tokensUsedThisMonth;
    }

    public void setTokensUsedThisMonth(Long tokensUsedThisMonth) {
        this.tokensUsedThisMonth = tokensUsedThisMonth;
    }

    public BigDecimal getMonthlyCostCapUsd() {
        return monthlyCostCapUsd;
    }

    public void setMonthlyCostCapUsd(BigDecimal monthlyCostCapUsd) {
        this.monthlyCostCapUsd = monthlyCostCapUsd;
    }

    public BigDecimal getCostAccumulatedUsd() {
        return costAccumulatedUsd;
    }

    public void setCostAccumulatedUsd(BigDecimal costAccumulatedUsd) {
        this.costAccumulatedUsd = costAccumulatedUsd;
    }

    public LocalDate getLastResetDate() {
        return lastResetDate;
    }

    public void setLastResetDate(LocalDate lastResetDate) {
        this.lastResetDate = lastResetDate;
    }

    public Boolean getIsThrottled() {
        return isThrottled;
    }

    public void setIsThrottled(Boolean isThrottled) {
        this.isThrottled = isThrottled;
    }

    public double getUsagePercent() {
        return usagePercent;
    }

    public void setUsagePercent(double usagePercent) {
        this.usagePercent = usagePercent;
    }

    public static class TenantQuotaDTOBuilder {
        private Long id;
        private Long tenantId;
        private Long monthlyTokenLimit;
        private Long tokensUsedThisMonth;
        private BigDecimal monthlyCostCapUsd;
        private BigDecimal costAccumulatedUsd;
        private LocalDate lastResetDate;
        private Boolean isThrottled;
        private double usagePercent;

        public TenantQuotaDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TenantQuotaDTOBuilder tenantId(Long tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public TenantQuotaDTOBuilder monthlyTokenLimit(Long monthlyTokenLimit) {
            this.monthlyTokenLimit = monthlyTokenLimit;
            return this;
        }

        public TenantQuotaDTOBuilder tokensUsedThisMonth(Long tokensUsedThisMonth) {
            this.tokensUsedThisMonth = tokensUsedThisMonth;
            return this;
        }

        public TenantQuotaDTOBuilder monthlyCostCapUsd(BigDecimal monthlyCostCapUsd) {
            this.monthlyCostCapUsd = monthlyCostCapUsd;
            return this;
        }

        public TenantQuotaDTOBuilder costAccumulatedUsd(BigDecimal costAccumulatedUsd) {
            this.costAccumulatedUsd = costAccumulatedUsd;
            return this;
        }

        public TenantQuotaDTOBuilder lastResetDate(LocalDate lastResetDate) {
            this.lastResetDate = lastResetDate;
            return this;
        }

        public TenantQuotaDTOBuilder isThrottled(Boolean isThrottled) {
            this.isThrottled = isThrottled;
            return this;
        }

        public TenantQuotaDTOBuilder usagePercent(double usagePercent) {
            this.usagePercent = usagePercent;
            return this;
        }

        public TenantQuotaDTO build() {
            return new TenantQuotaDTO(id, tenantId, monthlyTokenLimit, tokensUsedThisMonth,
                    monthlyCostCapUsd, costAccumulatedUsd, lastResetDate, isThrottled, usagePercent);
        }
    }
}
