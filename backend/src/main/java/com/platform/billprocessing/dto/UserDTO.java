package com.platform.billprocessing.dto;

import com.platform.billprocessing.model.Role;

import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private Long tenantId;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private Boolean enabled;
    private LocalDateTime createdAt;

    public UserDTO() {
    }

    public UserDTO(Long id, Long tenantId, String username, String email, String fullName,
                   Role role, Boolean enabled, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class UserDTOBuilder {
        private Long id;
        private Long tenantId;
        private String username;
        private String email;
        private String fullName;
        private Role role;
        private Boolean enabled;
        private LocalDateTime createdAt;

        public UserDTOBuilder id(Long id) { this.id = id; return this; }
        public UserDTOBuilder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public UserDTOBuilder username(String username) { this.username = username; return this; }
        public UserDTOBuilder email(String email) { this.email = email; return this; }
        public UserDTOBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserDTOBuilder role(Role role) { this.role = role; return this; }
        public UserDTOBuilder enabled(Boolean enabled) { this.enabled = enabled; return this; }
        public UserDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserDTO build() {
            return new UserDTO(id, tenantId, username, email, fullName, role, enabled, createdAt);
        }
    }
}
