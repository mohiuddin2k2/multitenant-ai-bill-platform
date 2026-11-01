package com.platform.billprocessing.security;

import com.platform.billprocessing.model.Organization;
import com.platform.billprocessing.model.User;
import com.platform.billprocessing.repository.OrganizationRepository;
import com.platform.billprocessing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        String orgPath = organizationRepository.findById(user.getTenantId())
                .map(Organization::getOrgPath)
                .orElse("/" + user.getTenantId() + "/");

        return CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .role(user.getRole())
                .tenantId(user.getTenantId())
                .orgPath(orgPath)
                .enabled(user.getEnabled())
                .build();
    }
}

