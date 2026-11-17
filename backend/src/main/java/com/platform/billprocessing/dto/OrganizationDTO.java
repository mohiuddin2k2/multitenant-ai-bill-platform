package com.platform.billprocessing.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrganizationDTO {
    private Long id;
    private String name;
    private String code;
    private Long parentId;
    private String orgPath;
    private List<OrganizationDTO> children;
    private LocalDateTime createdAt;

    public OrganizationDTO() {
    }

    public OrganizationDTO(Long id, String name, String code, Long parentId, String orgPath,
                           List<OrganizationDTO> children, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.parentId = parentId;
        this.orgPath = orgPath;
        this.children = children;
        this.createdAt = createdAt;
    }

    public static OrganizationDTOBuilder builder() {
        return new OrganizationDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getOrgPath() { return orgPath; }
    public void setOrgPath(String orgPath) { this.orgPath = orgPath; }
    public List<OrganizationDTO> getChildren() { return children; }
    public void setChildren(List<OrganizationDTO> children) { this.children = children; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class OrganizationDTOBuilder {
        private Long id;
        private String name;
        private String code;
        private Long parentId;
        private String orgPath;
        private List<OrganizationDTO> children;
        private LocalDateTime createdAt;

        public OrganizationDTOBuilder id(Long id) { this.id = id; return this; }
        public OrganizationDTOBuilder name(String name) { this.name = name; return this; }
        public OrganizationDTOBuilder code(String code) { this.code = code; return this; }
        public OrganizationDTOBuilder parentId(Long parentId) { this.parentId = parentId; return this; }
        public OrganizationDTOBuilder orgPath(String orgPath) { this.orgPath = orgPath; return this; }
        public OrganizationDTOBuilder children(List<OrganizationDTO> children) { this.children = children; return this; }
        public OrganizationDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public OrganizationDTO build() {
            return new OrganizationDTO(id, name, code, parentId, orgPath, children, createdAt);
        }
    }
}
