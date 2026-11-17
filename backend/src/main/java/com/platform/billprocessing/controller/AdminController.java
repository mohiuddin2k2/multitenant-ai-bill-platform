package com.platform.billprocessing.controller;

import com.platform.billprocessing.dto.BillAssignmentDTO;
import com.platform.billprocessing.dto.BillDTO;
import com.platform.billprocessing.service.BillService;
import com.platform.billprocessing.tenant.TenantContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final BillService billService;

    public AdminController(BillService billService) {
        this.billService = billService;
    }

    @PatchMapping("/bills/{id}/assign")
    public ResponseEntity<BillDTO> assignBillToClerk(
            @PathVariable Long id,
            @Valid @RequestBody BillAssignmentDTO request) {
        return ResponseEntity.ok(billService.assignToClerk(
                id, TenantContext.getTenantId(), TenantContext.getUserId(), request.getClerkUserId()));
    }
}
