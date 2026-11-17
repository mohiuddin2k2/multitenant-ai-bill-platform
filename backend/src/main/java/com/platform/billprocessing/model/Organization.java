package com.platform.billprocessing.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "org_path", nullable = false, length = 255)
    private String orgPath;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Organization() {
    }

    public Organization(Long id, String name, String code, Long parentId, String orgPath,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.parentId = parentId;
        this.orgPath = orgPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static OrganizationBuilder builder() {
        return new OrganizationBuilder();
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class OrganizationBuilder {
        private Long id;
        private String name;
        private String code;
        private Long parentId;
        private String orgPath;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public OrganizationBuilder id(Long id) { this.id = id; return this; }
        public OrganizationBuilder name(String name) { this.name = name; return this; }
        public OrganizationBuilder code(String code) { this.code = code; return this; }
        public OrganizationBuilder parentId(Long parentId) { this.parentId = parentId; return this; }
        public OrganizationBuilder orgPath(String orgPath) { this.orgPath = orgPath; return this; }
        public OrganizationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public OrganizationBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Organization build() {
            return new Organization(id, name, code, parentId, orgPath, createdAt, updatedAt);
        }
    }
}
