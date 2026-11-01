package com.platform.billprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}

