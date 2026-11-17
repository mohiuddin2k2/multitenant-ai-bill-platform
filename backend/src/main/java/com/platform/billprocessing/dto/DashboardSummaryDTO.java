package com.platform.billprocessing.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class DashboardSummaryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long tenantId;
    private String tenantName;
    private long totalBills;
    private long pendingBills;
    private long inReviewBills;
    private long approvedBills;
    private long rejectedBills;
    private long paidBills;
    private long anomalyCount;

    private BigDecimal totalSpend;
    private BigDecimal pendingSpend;
    private BigDecimal approvedSpend;

    private BigDecimal monthlyCostCapUsd;
    private BigDecimal costAccumulatedUsd;
    private double quotaRemainingPercent;

    private List<CategoryBreakdownDTO> categoryBreakdown;
    private List<TopVendorDTO> topVendors;

    public DashboardSummaryDTO() {
    }

    public DashboardSummaryDTO(Long tenantId, String tenantName, long totalBills, long pendingBills,
                               long inReviewBills, long approvedBills, long rejectedBills, long paidBills,
                               long anomalyCount, BigDecimal totalSpend, BigDecimal pendingSpend,
                               BigDecimal approvedSpend, BigDecimal monthlyCostCapUsd,
                               BigDecimal costAccumulatedUsd, double quotaRemainingPercent,
                               List<CategoryBreakdownDTO> categoryBreakdown, List<TopVendorDTO> topVendors) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.totalBills = totalBills;
        this.pendingBills = pendingBills;
        this.inReviewBills = inReviewBills;
        this.approvedBills = approvedBills;
        this.rejectedBills = rejectedBills;
        this.paidBills = paidBills;
        this.anomalyCount = anomalyCount;
        this.totalSpend = totalSpend;
        this.pendingSpend = pendingSpend;
        this.approvedSpend = approvedSpend;
        this.monthlyCostCapUsd = monthlyCostCapUsd;
        this.costAccumulatedUsd = costAccumulatedUsd;
        this.quotaRemainingPercent = quotaRemainingPercent;
        this.categoryBreakdown = categoryBreakdown;
        this.topVendors = topVendors;
    }

    public static DashboardSummaryDTOBuilder builder() {
        return new DashboardSummaryDTOBuilder();
    }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    public long getTotalBills() { return totalBills; }
    public void setTotalBills(long totalBills) { this.totalBills = totalBills; }
    public long getPendingBills() { return pendingBills; }
    public void setPendingBills(long pendingBills) { this.pendingBills = pendingBills; }
    public long getInReviewBills() { return inReviewBills; }
    public void setInReviewBills(long inReviewBills) { this.inReviewBills = inReviewBills; }
    public long getApprovedBills() { return approvedBills; }
    public void setApprovedBills(long approvedBills) { this.approvedBills = approvedBills; }
    public long getRejectedBills() { return rejectedBills; }
    public void setRejectedBills(long rejectedBills) { this.rejectedBills = rejectedBills; }
    public long getPaidBills() { return paidBills; }
    public void setPaidBills(long paidBills) { this.paidBills = paidBills; }
    public long getAnomalyCount() { return anomalyCount; }
    public void setAnomalyCount(long anomalyCount) { this.anomalyCount = anomalyCount; }
    public BigDecimal getTotalSpend() { return totalSpend; }
    public void setTotalSpend(BigDecimal totalSpend) { this.totalSpend = totalSpend; }
    public BigDecimal getPendingSpend() { return pendingSpend; }
    public void setPendingSpend(BigDecimal pendingSpend) { this.pendingSpend = pendingSpend; }
    public BigDecimal getApprovedSpend() { return approvedSpend; }
    public void setApprovedSpend(BigDecimal approvedSpend) { this.approvedSpend = approvedSpend; }
    public BigDecimal getMonthlyCostCapUsd() { return monthlyCostCapUsd; }
    public void setMonthlyCostCapUsd(BigDecimal monthlyCostCapUsd) { this.monthlyCostCapUsd = monthlyCostCapUsd; }
    public BigDecimal getCostAccumulatedUsd() { return costAccumulatedUsd; }
    public void setCostAccumulatedUsd(BigDecimal costAccumulatedUsd) { this.costAccumulatedUsd = costAccumulatedUsd; }
    public double getQuotaRemainingPercent() { return quotaRemainingPercent; }
    public void setQuotaRemainingPercent(double quotaRemainingPercent) { this.quotaRemainingPercent = quotaRemainingPercent; }
    public List<CategoryBreakdownDTO> getCategoryBreakdown() { return categoryBreakdown; }
    public void setCategoryBreakdown(List<CategoryBreakdownDTO> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }
    public List<TopVendorDTO> getTopVendors() { return topVendors; }
    public void setTopVendors(List<TopVendorDTO> topVendors) { this.topVendors = topVendors; }

    public static class DashboardSummaryDTOBuilder {
        private Long tenantId;
        private String tenantName;
        private long totalBills;
        private long pendingBills;
        private long inReviewBills;
        private long approvedBills;
        private long rejectedBills;
        private long paidBills;
        private long anomalyCount;
        private BigDecimal totalSpend;
        private BigDecimal pendingSpend;
        private BigDecimal approvedSpend;
        private BigDecimal monthlyCostCapUsd;
        private BigDecimal costAccumulatedUsd;
        private double quotaRemainingPercent;
        private List<CategoryBreakdownDTO> categoryBreakdown;
        private List<TopVendorDTO> topVendors;

        public DashboardSummaryDTOBuilder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public DashboardSummaryDTOBuilder tenantName(String tenantName) { this.tenantName = tenantName; return this; }
        public DashboardSummaryDTOBuilder totalBills(long totalBills) { this.totalBills = totalBills; return this; }
        public DashboardSummaryDTOBuilder pendingBills(long pendingBills) { this.pendingBills = pendingBills; return this; }
        public DashboardSummaryDTOBuilder inReviewBills(long inReviewBills) { this.inReviewBills = inReviewBills; return this; }
        public DashboardSummaryDTOBuilder approvedBills(long approvedBills) { this.approvedBills = approvedBills; return this; }
        public DashboardSummaryDTOBuilder rejectedBills(long rejectedBills) { this.rejectedBills = rejectedBills; return this; }
        public DashboardSummaryDTOBuilder paidBills(long paidBills) { this.paidBills = paidBills; return this; }
        public DashboardSummaryDTOBuilder anomalyCount(long anomalyCount) { this.anomalyCount = anomalyCount; return this; }
        public DashboardSummaryDTOBuilder totalSpend(BigDecimal totalSpend) { this.totalSpend = totalSpend; return this; }
        public DashboardSummaryDTOBuilder pendingSpend(BigDecimal pendingSpend) { this.pendingSpend = pendingSpend; return this; }
        public DashboardSummaryDTOBuilder approvedSpend(BigDecimal approvedSpend) { this.approvedSpend = approvedSpend; return this; }
        public DashboardSummaryDTOBuilder monthlyCostCapUsd(BigDecimal monthlyCostCapUsd) { this.monthlyCostCapUsd = monthlyCostCapUsd; return this; }
        public DashboardSummaryDTOBuilder costAccumulatedUsd(BigDecimal costAccumulatedUsd) { this.costAccumulatedUsd = costAccumulatedUsd; return this; }
        public DashboardSummaryDTOBuilder quotaRemainingPercent(double quotaRemainingPercent) { this.quotaRemainingPercent = quotaRemainingPercent; return this; }
        public DashboardSummaryDTOBuilder categoryBreakdown(List<CategoryBreakdownDTO> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; return this; }
        public DashboardSummaryDTOBuilder topVendors(List<TopVendorDTO> topVendors) { this.topVendors = topVendors; return this; }

        public DashboardSummaryDTO build() {
            return new DashboardSummaryDTO(tenantId, tenantName, totalBills, pendingBills, inReviewBills,
                    approvedBills, rejectedBills, paidBills, anomalyCount, totalSpend, pendingSpend,
                    approvedSpend, monthlyCostCapUsd, costAccumulatedUsd, quotaRemainingPercent,
                    categoryBreakdown, topVendors);
        }
    }
}
