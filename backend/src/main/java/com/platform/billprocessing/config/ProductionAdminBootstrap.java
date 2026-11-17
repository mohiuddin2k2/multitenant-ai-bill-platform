package com.platform.billprocessing.config;

import com.platform.billprocessing.model.User;
import com.platform.billprocessing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Secures the seeded development accounts when the production profile is enabled. */
@Component
@Profile("production")
public class ProductionAdminBootstrap implements ApplicationRunner {

    private static final String ADMIN_EMAIL = "admin@shaksham.com";
    private static final List<String> DEMO_EMAILS = List.of(
            "north_admin@shaksham.com",
            "clerk_north@shaksham.com",
            "approver_north@shaksham.com",
            "south_admin@shaksham.com",
            "clerk_south@shaksham.com",
            "approver_south@shaksham.com"
    );

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;

    public ProductionAdminBootstrap(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${BOOTSTRAP_ADMIN_PASSWORD:}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (adminPassword == null || adminPassword.length() < 12) {
            throw new IllegalStateException("BOOTSTRAP_ADMIN_PASSWORD must be set to at least 12 characters.");
        }

        User admin = userRepository.findByEmail(ADMIN_EMAIL)
                .orElseThrow(() -> new IllegalStateException("Seeded platform admin was not found."));
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setEnabled(true);
        userRepository.save(admin);

        for (String demoEmail : DEMO_EMAILS) {
            userRepository.findByEmail(demoEmail).ifPresent(user -> {
                user.setEnabled(false);
                userRepository.save(user);
            });
        }
    }
}
