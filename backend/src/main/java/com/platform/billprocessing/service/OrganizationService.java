package com.platform.billprocessing.service;

import com.platform.billprocessing.dto.OrganizationDTO;
import com.platform.billprocessing.exception.ResourceNotFoundException;
import com.platform.billprocessing.model.Organization;
import com.platform.billprocessing.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional(readOnly = true)
    public List<OrganizationDTO> getAllOrganizations() {
        List<Organization> orgs = organizationRepository.findAll();
        return buildHierarchyTree(orgs);
    }

    @Transactional(readOnly = true)
    public OrganizationDTO getOrganizationById(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with ID: " + id));
        return mapToDTO(org);
    }

    @Transactional(readOnly = true)
    public List<OrganizationDTO> getSubOrganizations(Long tenantId) {
        Organization parent = organizationRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent organization not found"));
        List<Organization> subtree = organizationRepository.findAllByOrgPathStartingWith(parent.getOrgPath());
        return subtree.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public OrganizationDTO createOrganization(String name, String code, Long parentId) {
        Organization parent = null;
        if (parentId != null) {
            parent = organizationRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent organization not found: " + parentId));
        }

        Organization org = Organization.builder()
                .name(name)
                .code(code)
                .parentId(parentId)
                .orgPath("/temp/")
                .build();
        org = organizationRepository.save(org);

        String path = (parent != null ? parent.getOrgPath() : "/") + org.getId() + "/";
        org.setOrgPath(path);
        org = organizationRepository.save(org);

        return mapToDTO(org);
    }

    public boolean canAccessTenant(String userOrgPath, Long targetTenantId) {
        if (userOrgPath == null || targetTenantId == null) {
            return false;
        }
        Organization target = organizationRepository.findById(targetTenantId).orElse(null);
        if (target == null) {
            return false;
        }
        return target.getOrgPath().startsWith(userOrgPath);
    }

    private List<OrganizationDTO> buildHierarchyTree(List<Organization> allOrgs) {
        Map<Long, OrganizationDTO> dtoMap = allOrgs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toMap(OrganizationDTO::getId, dto -> dto));

        List<OrganizationDTO> rootOrgs = new ArrayList<>();
        for (OrganizationDTO dto : dtoMap.values()) {
            if (dto.getParentId() == null || !dtoMap.containsKey(dto.getParentId())) {
                rootOrgs.add(dto);
            } else {
                OrganizationDTO parent = dtoMap.get(dto.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(dto);
            }
        }
        return rootOrgs;
    }

    private OrganizationDTO mapToDTO(Organization o) {
        return OrganizationDTO.builder()
                .id(o.getId())
                .name(o.getName())
                .code(o.getCode())
                .parentId(o.getParentId())
                .orgPath(o.getOrgPath())
                .createdAt(o.getCreatedAt())
                .build();
    }
}

