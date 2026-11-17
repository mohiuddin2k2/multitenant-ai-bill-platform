package com.platform.billprocessing.security;

import com.platform.billprocessing.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final Role role;
    private final Long tenantId;
    private final String orgPath;
    private final boolean enabled;

    public CustomUserDetails(Long id, String username, String email, String password, Role role,
                             Long tenantId, String orgPath, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.tenantId = tenantId;
        this.orgPath = orgPath;
        this.enabled = enabled;
    }

    public static CustomUserDetailsBuilder builder() {
        return new CustomUserDetailsBuilder();
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Long getTenantId() { return tenantId; }
    public String getOrgPath() { return orgPath; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public static class CustomUserDetailsBuilder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private Role role;
        private Long tenantId;
        private String orgPath;
        private boolean enabled = true;

        public CustomUserDetailsBuilder id(Long id) { this.id = id; return this; }
        public CustomUserDetailsBuilder username(String username) { this.username = username; return this; }
        public CustomUserDetailsBuilder email(String email) { this.email = email; return this; }
        public CustomUserDetailsBuilder password(String password) { this.password = password; return this; }
        public CustomUserDetailsBuilder role(Role role) { this.role = role; return this; }
        public CustomUserDetailsBuilder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public CustomUserDetailsBuilder orgPath(String orgPath) { this.orgPath = orgPath; return this; }
        public CustomUserDetailsBuilder enabled(boolean enabled) { this.enabled = enabled; return this; }

        public CustomUserDetails build() {
            return new CustomUserDetails(id, username, email, password, role, tenantId, orgPath, enabled);
        }
    }
}
