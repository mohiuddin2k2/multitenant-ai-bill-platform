package com.platform.billprocessing.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tenant_quotas")
public class TenantQuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false, unique = true)
    private Long tenantId;

    @Column(name = "monthly_token_limit", nullable = false)
    private Long monthlyTokenLimit = 1000000L;

    @Column(name = "tokens_used_this_month", nullable = false)
    private Long tokensUsedThisMonth = 0L;

    @Column(name = "monthly_cost_cap_usd", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyCostCapUsd = new BigDecimal("50.00");

    @Column(name = "cost_accumulated_usd", nullable = false, precision = 10, scale = 4)
    private BigDecimal costAccumulatedUsd = BigDecimal.ZERO;

    @Column(name = "last_reset_date", nullable = false)
    private LocalDate lastResetDate = LocalDate.now();

    @Column(name = "is_throttled", nullable = false)
    private Boolean isThrottled = false;

    public TenantQuota() {
    }

    public TenantQuota(Long id, Long tenantId, Long monthlyTokenLimit, Long tokensUsedThisMonth,
                       BigDecimal monthlyCostCapUsd, BigDecimal costAccumulatedUsd,
                       LocalDate lastResetDate, Boolean isThrottled) {
        this.id = id;
        this.tenantId = tenantId;
        this.monthlyTokenLimit = monthlyTokenLimit != null ? monthlyTokenLimit : 1000000L;
        this.tokensUsedThisMonth = tokensUsedThisMonth != null ? tokensUsedThisMonth : 0L;
        this.monthlyCostCapUsd = monthlyCostCapUsd != null ? monthlyCostCapUsd : new BigDecimal("50.00");
        this.costAccumulatedUsd = costAccumulatedUsd != null ? costAccumulatedUsd : BigDecimal.ZERO;
        this.lastResetDate = lastResetDate != null ? lastResetDate : LocalDate.now();
        this.isThrottled = isThrottled != null ? isThrottled : false;
    }

    public static TenantQuotaBuilder builder() {
        return new TenantQuotaBuilder();
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

    public static class TenantQuotaBuilder {
        private Long id;
        private Long tenantId;
        private Long monthlyTokenLimit = 1000000L;
        private Long tokensUsedThisMonth = 0L;
        private BigDecimal monthlyCostCapUsd = new BigDecimal("50.00");
        private BigDecimal costAccumulatedUsd = BigDecimal.ZERO;
        private LocalDate lastResetDate = LocalDate.now();
        private Boolean isThrottled = false;

        public TenantQuotaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TenantQuotaBuilder tenantId(Long tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public TenantQuotaBuilder monthlyTokenLimit(Long monthlyTokenLimit) {
            this.monthlyTokenLimit = monthlyTokenLimit;
            return this;
        }

        public TenantQuotaBuilder tokensUsedThisMonth(Long tokensUsedThisMonth) {
            this.tokensUsedThisMonth = tokensUsedThisMonth;
            return this;
        }

        public TenantQuotaBuilder monthlyCostCapUsd(BigDecimal monthlyCostCapUsd) {
            this.monthlyCostCapUsd = monthlyCostCapUsd;
            return this;
        }

        public TenantQuotaBuilder costAccumulatedUsd(BigDecimal costAccumulatedUsd) {
            this.costAccumulatedUsd = costAccumulatedUsd;
            return this;
        }

        public TenantQuotaBuilder lastResetDate(LocalDate lastResetDate) {
            this.lastResetDate = lastResetDate;
            return this;
        }

        public TenantQuotaBuilder isThrottled(Boolean isThrottled) {
            this.isThrottled = isThrottled;
            return this;
        }

        public TenantQuota build() {
            return new TenantQuota(id, tenantId, monthlyTokenLimit, tokensUsedThisMonth,
                    monthlyCostCapUsd, costAccumulatedUsd, lastResetDate, isThrottled);
        }
    }
}
