package com.platform.billprocessing.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tenant_quotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantQuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false, unique = true)
    private Long tenantId;

    @Column(name = "monthly_token_limit", nullable = false)
    @Builder.Default
    private Long monthlyTokenLimit = 1000000L;

    @Column(name = "tokens_used_this_month", nullable = false)
    @Builder.Default
    private Long tokensUsedThisMonth = 0L;

    @Column(name = "monthly_cost_cap_usd", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal monthlyCostCapUsd = new BigDecimal("50.00");

    @Column(name = "cost_accumulated_usd", nullable = false, precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal costAccumulatedUsd = BigDecimal.ZERO;

    @Column(name = "last_reset_date", nullable = false)
    @Builder.Default
    private LocalDate lastResetDate = LocalDate.now();

    @Column(name = "is_throttled", nullable = false)
    @Builder.Default
    private Boolean isThrottled = false;
}

