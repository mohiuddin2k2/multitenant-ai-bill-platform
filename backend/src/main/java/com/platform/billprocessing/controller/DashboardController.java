package com.platform.billprocessing.controller;

import com.platform.billprocessing.dto.DashboardSummaryDTO;
import com.platform.billprocessing.service.DashboardService;
import com.platform.billprocessing.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard & Analytics", description = "Tenant Overview, Processed vs Pending Analytics, Redis Cached")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get aggregated summary statistics for the active tenant")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        Long tenantId = TenantContext.getTenantId();
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(tenantId);
        return ResponseEntity.ok(summary);
    }
}

