package com.platform.billprocessing.controller;

import com.platform.billprocessing.dto.TenantQuotaDTO;
import com.platform.billprocessing.service.TenantQuotaService;
import com.platform.billprocessing.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/quotas")
@Tag(name = "AI Cost Capping & Quotas", description = "Tenant AI Token Budgets and Cost Safeguards")
public class TenantQuotaController {

    private final TenantQuotaService tenantQuotaService;

    public TenantQuotaController(TenantQuotaService tenantQuotaService) {
        this.tenantQuotaService = tenantQuotaService;
    }

    @GetMapping("/my-quota")
    @Operation(summary = "Get current tenant AI usage, cost cap and throttle status")
    public ResponseEntity<TenantQuotaDTO> getMyQuota() {
        Long tenantId = TenantContext.getTenantId();
        TenantQuotaDTO quota = tenantQuotaService.getQuota(tenantId);
        return ResponseEntity.ok(quota);
    }

    @PutMapping("/{tenantId}/limits")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update monthly token and cost limits for a tenant (Admin only)")
    public ResponseEntity<TenantQuotaDTO> updateLimits(
            @PathVariable Long tenantId,
            @RequestParam(required = false) Long monthlyTokenLimit,
            @RequestParam(required = false) BigDecimal monthlyCostCapUsd) {

        TenantQuotaDTO updated = tenantQuotaService.updateQuotaLimits(tenantId, monthlyTokenLimit, monthlyCostCapUsd);
        return ResponseEntity.ok(updated);
    }
}

