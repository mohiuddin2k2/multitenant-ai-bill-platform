package com.platform.billprocessing.service;

import com.platform.billprocessing.dto.CategoryBreakdownDTO;
import com.platform.billprocessing.dto.DashboardSummaryDTO;
import com.platform.billprocessing.dto.TenantQuotaDTO;
import com.platform.billprocessing.dto.TopVendorDTO;
import com.platform.billprocessing.model.BillStatus;
import com.platform.billprocessing.model.Organization;
import com.platform.billprocessing.repository.BillRepository;
import com.platform.billprocessing.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final BillRepository billRepository;
    private final OrganizationRepository organizationRepository;
    private final TenantQuotaService tenantQuotaService;

    @Transactional(readOnly = true)
    @Cacheable(value = "dashboard-summary", key = "#tenantId", unless = "#result == null")
    public DashboardSummaryDTO getDashboardSummary(Long tenantId) {
        log.info("Computing fresh dashboard analytics for tenant: {}", tenantId);

        String tenantName = organizationRepository.findById(tenantId)
                .map(Organization::getName)
                .orElse("Tenant " + tenantId);

        long total = billRepository.countByTenantId(tenantId);
        long pending = billRepository.countByTenantIdAndStatus(tenantId, BillStatus.PENDING);
        long inReview = billRepository.countByTenantIdAndStatus(tenantId, BillStatus.IN_REVIEW);
        long approved = billRepository.countByTenantIdAndStatus(tenantId, BillStatus.APPROVED);
        long rejected = billRepository.countByTenantIdAndStatus(tenantId, BillStatus.REJECTED);
        long paid = billRepository.countByTenantIdAndStatus(tenantId, BillStatus.PAID);
        long anomalies = billRepository.countByTenantIdAndIsAnomalyTrue(tenantId);

        BigDecimal totalSpend = billRepository.sumTotalSpendByTenantId(tenantId);
        BigDecimal pendingSpend = billRepository.sumTotalSpendByTenantIdAndStatus(tenantId, BillStatus.PENDING);
        BigDecimal approvedSpend = billRepository.sumTotalSpendByTenantIdAndStatus(tenantId, BillStatus.APPROVED);

        TenantQuotaDTO quota = tenantQuotaService.getQuota(tenantId);

        // Category Breakdown
        List<Object[]> catRows = billRepository.getCategoryBreakdownByTenantId(tenantId);
        List<CategoryBreakdownDTO> categoryBreakdown = new ArrayList<>();
        for (Object[] row : catRows) {
            categoryBreakdown.add(CategoryBreakdownDTO.builder()
                    .category((String) row[0])
                    .billCount(((Number) row[1]).longValue())
                    .totalAmount((BigDecimal) row[2])
                    .build());
        }

        // Top Vendors
        List<Object[]> vendorRows = billRepository.getTopVendorsByTenantId(tenantId, PageRequest.of(0, 5));
        List<TopVendorDTO> topVendors = new ArrayList<>();
        for (Object[] row : vendorRows) {
            topVendors.add(TopVendorDTO.builder()
                    .vendorName((String) row[0])
                    .billCount(((Number) row[1]).longValue())
                    .totalAmount((BigDecimal) row[2])
                    .build());
        }

        return DashboardSummaryDTO.builder()
                .tenantId(tenantId)
                .tenantName(tenantName)
                .totalBills(total)
                .pendingBills(pending)
                .inReviewBills(inReview)
                .approvedBills(approved)
                .rejectedBills(rejected)
                .paidBills(paid)
                .anomalyCount(anomalies)
                .totalSpend(totalSpend)
                .pendingSpend(pendingSpend)
                .approvedSpend(approvedSpend)
                .monthlyCostCapUsd(quota.getMonthlyCostCapUsd())
                .costAccumulatedUsd(quota.getCostAccumulatedUsd())
                .quotaRemainingPercent(Math.max(0.0, 100.0 - quota.getUsagePercent()))
                .categoryBreakdown(categoryBreakdown)
                .topVendors(topVendors)
                .build();
    }
}

