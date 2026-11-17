package com.platform.billprocessing.controller;

import com.platform.billprocessing.dto.*;
import com.platform.billprocessing.exception.RateLimitExceededException;
import com.platform.billprocessing.model.BillStatus;
import com.platform.billprocessing.service.BillService;
import com.platform.billprocessing.service.NaturalLanguageQueryService;
import com.platform.billprocessing.service.RateLimitingService;
import com.platform.billprocessing.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
@Tag(name = "Bills Management", description = "Multi-Tenant Bill Lifecycle, Submissions, Approvals and NL Search")
public class BillController {

    private final BillService billService;
    private final NaturalLanguageQueryService naturalLanguageQueryService;
    private final RateLimitingService rateLimitingService;

    public BillController(
            BillService billService,
            NaturalLanguageQueryService naturalLanguageQueryService,
            RateLimitingService rateLimitingService) {
        this.billService = billService;
        this.naturalLanguageQueryService = naturalLanguageQueryService;
        this.rateLimitingService = rateLimitingService;
    }

    @GetMapping
    @Operation(summary = "Get paginated bills isolated to current tenant")
    public ResponseEntity<Page<BillDTO>> getBills(
            @RequestParam(required = false) BillStatus status,
            @PageableDefault(size = 10, sort = "billDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Long tenantId = TenantContext.getTenantId();
        checkRateLimit(tenantId);
        Page<BillDTO> bills = billService.getBillsForUser(tenantId, TenantContext.getUserId(), TenantContext.getUserRole(), status, pageable);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get single bill details with strict tenant boundary check")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        Long tenantId = TenantContext.getTenantId();
        checkRateLimit(tenantId);
        BillDTO bill = billService.getBillById(id, tenantId);
        return ResponseEntity.ok(bill);
    }

    @PostMapping
    @Operation(summary = "Submit a bill with automated duplicate and anomaly detection")
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillCreateDTO createDTO) {
        Long tenantId = TenantContext.getTenantId();
        Long userId = TenantContext.getUserId();
        checkRateLimit(tenantId);

        BillDTO created = billService.createBill(createDTO, tenantId, userId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update bill details while it is assigned to the current clerk")
    public ResponseEntity<BillDTO> updateAssignedBill(
            @PathVariable Long id,
            @Valid @RequestBody BillCreateDTO updateDTO) {
        BillDTO updated = billService.updateAssignedBillDetails(
                id, TenantContext.getTenantId(), TenantContext.getUserId(), updateDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update bill status (Approve, Reject, Pay) with RBAC validation")
    public ResponseEntity<BillDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody BillStatusUpdateDTO updateDTO) {

        Long tenantId = TenantContext.getTenantId();
        Long userId = TenantContext.getUserId();
        String userRole = TenantContext.getUserRole();

        BillDTO updated = billService.updateStatus(id, tenantId, userId, userRole, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/search/natural-language")
    @Operation(summary = "Natural-language query converted to safe parameterized JPA Criteria specification")
    public ResponseEntity<NaturalLanguageQueryResponseDTO> naturalLanguageSearch(
            @Valid @RequestBody NaturalLanguageQueryRequestDTO request) {

        Long tenantId = TenantContext.getTenantId();
        checkRateLimit(tenantId);

        NaturalLanguageQueryResponseDTO response = naturalLanguageQueryService.executeNaturalLanguageQuery(tenantId, request.getPrompt());
        return ResponseEntity.ok(response);
    }

    private void checkRateLimit(Long tenantId) {
        if (!rateLimitingService.tryConsume(tenantId)) {
            throw new RateLimitExceededException("Rate limit exceeded for tenant " + tenantId + ". Please retry in a few moments.");
        }
    }
}

