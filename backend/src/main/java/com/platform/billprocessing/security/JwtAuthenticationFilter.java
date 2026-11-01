package com.platform.billprocessing.security;

import com.platform.billprocessing.tenant.TenantContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Claims claims = tokenProvider.getClaimsFromToken(jwt);
                String username = claims.getSubject();

                Long tenantId = null;
                Object tenantIdClaim = claims.get("tenantId");
                if (tenantIdClaim instanceof Number) {
                    tenantId = ((Number) tenantIdClaim).longValue();
                }

                String orgPath = (String) claims.get("orgPath");
                String role = (String) claims.get("role");
                Long userId = null;
                Object userIdClaim = claims.get("userId");
                if (userIdClaim instanceof Number) {
                    userId = ((Number) userIdClaim).longValue();
                }

                // Support authorized HQ tenant override header (e.g. for parent organization viewing sub-tenant)
                String overrideTenantHeader = request.getHeader("X-Tenant-Override");
                if (StringUtils.hasText(overrideTenantHeader) && "ADMIN".equalsIgnoreCase(role)) {
                    try {
                        Long overrideTenantId = Long.parseLong(overrideTenantHeader);
                        tenantId = overrideTenantId;
                    } catch (NumberFormatException ignored) {}
                }

                TenantContext.setTenantId(tenantId);
                TenantContext.setOrgPath(orgPath);
                TenantContext.setUserId(userId);
                TenantContext.setUserRole(role);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}

