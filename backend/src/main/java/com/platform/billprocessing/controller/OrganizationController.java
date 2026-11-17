package com.platform.billprocessing.controller;

import com.platform.billprocessing.dto.OrganizationDTO;
import com.platform.billprocessing.service.OrganizationService;
import com.platform.billprocessing.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
@Tag(name = "Organizations Hierarchy", description = "Parent and Sub-Organization Multi-Tenant Structure")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    @Operation(summary = "Get full organization tree structure")
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        List<OrganizationDTO> orgs = organizationService.getAllOrganizations();
        return ResponseEntity.ok(orgs);
    }

    @GetMapping("/my-tree")
    @Operation(summary = "Get sub-organizations within current tenant hierarchy")
    public ResponseEntity<List<OrganizationDTO>> getMySubTree() {
        Long tenantId = TenantContext.getTenantId();
        List<OrganizationDTO> tree = organizationService.getSubOrganizations(tenantId);
        return ResponseEntity.ok(tree);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a sub-organization (Admin only)")
    public ResponseEntity<OrganizationDTO> createOrganization(
            @RequestParam String name,
            @RequestParam String code,
            @RequestParam(required = false) Long parentId) {

        OrganizationDTO created = organizationService.createOrganization(name, code, parentId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}

