package com.platform.billprocessing.controller;

import com.platform.billprocessing.dto.AuthRequest;
import com.platform.billprocessing.dto.AuthResponse;
import com.platform.billprocessing.dto.RegisterRequest;
import com.platform.billprocessing.dto.UserDTO;
import com.platform.billprocessing.security.CustomUserDetails;
import com.platform.billprocessing.service.AuthService;
import com.platform.billprocessing.tenant.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "JWT Token Generation and User Onboarding")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and issue multi-tenant JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user within a tenant")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = authService.register(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated principal and tenant context")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", userDetails.getId());
        map.put("username", userDetails.getUsername());
        map.put("email", userDetails.getEmail());
        map.put("role", userDetails.getRole());
        map.put("tenantId", TenantContext.getTenantId());
        map.put("orgPath", userDetails.getOrgPath());
        return ResponseEntity.ok(map);
    }
}

