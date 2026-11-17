package com.platform.billprocessing.service;

import com.platform.billprocessing.dto.AuthRequest;
import com.platform.billprocessing.dto.AuthResponse;
import com.platform.billprocessing.dto.RegisterRequest;
import com.platform.billprocessing.dto.UserDTO;
import com.platform.billprocessing.exception.ResourceNotFoundException;
import com.platform.billprocessing.model.Organization;
import com.platform.billprocessing.model.User;
import com.platform.billprocessing.repository.OrganizationRepository;
import com.platform.billprocessing.repository.UserRepository;
import com.platform.billprocessing.security.CustomUserDetails;
import com.platform.billprocessing.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = tokenProvider.generateToken(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getRole().name(),
                userDetails.getTenantId(),
                userDetails.getOrgPath()
        );

        String orgName = organizationRepository.findById(userDetails.getTenantId())
                .map(Organization::getName)
                .orElse("Default Organization");

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .fullName(userDetails.getUsername())
                .role(userDetails.getRole())
                .tenantId(userDetails.getTenantId())
                .organizationName(orgName)
                .orgPath(userDetails.getOrgPath())
                .build();
    }

    @Transactional
    public UserDTO register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        organizationRepository.findById(request.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with ID: " + request.getTenantId()));

        User user = User.builder()
                .tenantId(request.getTenantId())
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole())
                .enabled(true)
                .build();

        user = userRepository.save(user);

        return toUserDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(Long tenantId) {
        return userRepository.findByTenantId(tenantId).stream().map(this::toUserDTO).toList();
    }

    private UserDTO toUserDTO(User user) {
        return UserDTO.builder().id(user.getId()).tenantId(user.getTenantId())
                .username(user.getUsername()).email(user.getEmail()).fullName(user.getFullName())
                .role(user.getRole()).enabled(user.getEnabled()).createdAt(user.getCreatedAt()).build();
    }
}

