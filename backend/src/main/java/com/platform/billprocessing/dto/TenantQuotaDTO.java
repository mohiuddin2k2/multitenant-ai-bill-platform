package com.platform.billprocessing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}

