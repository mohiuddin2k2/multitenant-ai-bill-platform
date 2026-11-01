package com.platform.billprocessing.service;

import com.platform.billprocessing.dto.TenantQuotaDTO;
import com.platform.billprocessing.exception.QuotaExceededException;
import com.platform.billprocessing.exception.ResourceNotFoundException;
import com.platform.billprocessing.model.TenantQuota;
import com.platform.billprocessing.repository.TenantQuotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantQuotaService {

    private final TenantQuotaRepository tenantQuotaRepository;

    @Value("${app.ai.cost-per-1k-tokens:0.00015}")
    private double costPer1kTokens;

    @Transactional(readOnly = true)
    public TenantQuotaDTO getQuota(Long tenantId) {
        TenantQuota quota = getOrCreateQuota(tenantId);
        return mapToDTO(quota);
    }

    @Transactional
    public void validateQuotaAvailable(Long tenantId) {
        TenantQuota quota = getOrCreateQuota(tenantId);
        checkMonthlyReset(quota);

        if (Boolean.TRUE.equals(quota.getIsThrottled())) {
            throw new QuotaExceededException("AI processing for tenant " + tenantId + " is currently throttled due to administrative action.");
        }

        if (quota.getTokensUsedThisMonth() >= quota.getMonthlyTokenLimit()) {
            throw new QuotaExceededException("Monthly AI token limit of " + quota.getMonthlyTokenLimit() + " reached for tenant " + tenantId + ".");
        }

        if (quota.getCostAccumulatedUsd().compareTo(quota.getMonthlyCostCapUsd()) >= 0) {
            throw new QuotaExceededException("Monthly AI cost cap of $" + quota.getMonthlyCostCapUsd() + " exceeded for tenant " + tenantId + ".");
        }
    }

    @Transactional
    public void recordAiUsage(Long tenantId, int tokensConsumed) {
        TenantQuota quota = getOrCreateQuota(tenantId);
        checkMonthlyReset(quota);

        long newTokens = quota.getTokensUsedThisMonth() + tokensConsumed;
        quota.setTokensUsedThisMonth(newTokens);

        BigDecimal incrementalCost = BigDecimal.valueOf((tokensConsumed / 1000.0) * costPer1kTokens)
                .setScale(4, RoundingMode.HALF_UP);
        BigDecimal newCost = quota.getCostAccumulatedUsd().add(incrementalCost);
        quota.setCostAccumulatedUsd(newCost);

        if (newCost.compareTo(quota.getMonthlyCostCapUsd()) >= 0 || newTokens >= quota.getMonthlyTokenLimit()) {
            quota.setIsThrottled(true);
            log.warn("Tenant {} has reached its AI quota limit! Tokens: {}, Cost: ${}", tenantId, newTokens, newCost);
        }

        tenantQuotaRepository.save(quota);
    }

    @Transactional
    public TenantQuotaDTO updateQuotaLimits(Long tenantId, Long newMonthlyTokenLimit, BigDecimal newMonthlyCostCapUsd) {
        TenantQuota quota = getOrCreateQuota(tenantId);
        if (newMonthlyTokenLimit != null) {
            quota.setMonthlyTokenLimit(newMonthlyTokenLimit);
        }
        if (newMonthlyCostCapUsd != null) {
            quota.setMonthlyCostCapUsd(newMonthlyCostCapUsd);
        }
        if (quota.getCostAccumulatedUsd().compareTo(quota.getMonthlyCostCapUsd()) < 0 &&
            quota.getTokensUsedThisMonth() < quota.getMonthlyTokenLimit()) {
            quota.setIsThrottled(false);
        }
        quota = tenantQuotaRepository.save(quota);
        return mapToDTO(quota);
    }

    private TenantQuota getOrCreateQuota(Long tenantId) {
        return tenantQuotaRepository.findByTenantId(tenantId).orElseGet(() -> {
            TenantQuota newQuota = TenantQuota.builder()
                    .tenantId(tenantId)
                    .monthlyTokenLimit(1000000L)
                    .tokensUsedThisMonth(0L)
                    .monthlyCostCapUsd(new BigDecimal("50.00"))
                    .costAccumulatedUsd(BigDecimal.ZERO)
                    .lastResetDate(LocalDate.now())
                    .isThrottled(false)
                    .build();
            return tenantQuotaRepository.save(newQuota);
        });
    }

    private void checkMonthlyReset(TenantQuota quota) {
        LocalDate now = LocalDate.now();
        if (quota.getLastResetDate() == null ||
            quota.getLastResetDate().getMonthValue() != now.getMonthValue() ||
            quota.getLastResetDate().getYear() != now.getYear()) {
            quota.setTokensUsedThisMonth(0L);
            quota.setCostAccumulatedUsd(BigDecimal.ZERO);
            quota.setIsThrottled(false);
            quota.setLastResetDate(now);
            tenantQuotaRepository.save(quota);
            log.info("Reset AI quota cycle for tenant {}", quota.getTenantId());
        }
    }

    private TenantQuotaDTO mapToDTO(TenantQuota q) {
        double tokenPct = q.getMonthlyTokenLimit() > 0 ?
                (q.getTokensUsedThisMonth() * 100.0) / q.getMonthlyTokenLimit() : 0.0;
        double costPct = q.getMonthlyCostCapUsd().compareTo(BigDecimal.ZERO) > 0 ?
                (q.getCostAccumulatedUsd().doubleValue() * 100.0) / q.getMonthlyCostCapUsd().doubleValue() : 0.0;
        double maxPct = Math.min(100.0, Math.max(tokenPct, costPct));

        return TenantQuotaDTO.builder()
                .id(q.getId())
                .tenantId(q.getTenantId())
                .monthlyTokenLimit(q.getMonthlyTokenLimit())
                .tokensUsedThisMonth(q.getTokensUsedThisMonth())
                .monthlyCostCapUsd(q.getMonthlyCostCapUsd())
                .costAccumulatedUsd(q.getCostAccumulatedUsd())
                .lastResetDate(q.getLastResetDate())
                .isThrottled(q.getIsThrottled())
                .usagePercent(Math.round(maxPct * 10.0) / 10.0)
                .build();
    }
}

