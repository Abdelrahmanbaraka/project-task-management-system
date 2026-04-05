package com.abdelrahman.backend.init;

import com.abdelrahman.backend.entity.Role;
import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.enums.RoleName;
import com.abdelrahman.backend.repository.RoleRepository;
import com.abdelrahman.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("=== DataInitializer started ===");
        seedRoles();
        seedUsers();
        System.out.println("=== DataInitializer finished ===");
    }

    private void seedRoles() {
        if (roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            roleRepository.save(Role.builder().name(RoleName.ADMIN).build());
        }

        if (roleRepository.findByName(RoleName.PROJECT_LEADER).isEmpty()) {
            roleRepository.save(Role.builder().name(RoleName.PROJECT_LEADER).build());
        }

        if (roleRepository.findByName(RoleName.EMPLOYEE).isEmpty()) {
            roleRepository.save(Role.builder().name(RoleName.EMPLOYEE).build());
        }
    }

    private void seedUsers() {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();
        Role leaderRole = roleRepository.findByName(RoleName.PROJECT_LEADER).orElseThrow();
        Role employeeRole = roleRepository.findByName(RoleName.EMPLOYEE).orElseThrow();

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("Admin123!"))
                    .active(true)
                    .role(adminRole)
                    .build());
        }

        if (!userRepository.existsByUsername("leader")) {
            userRepository.save(User.builder()
                    .username("leader")
                    .email("leader@example.com")
                    .password(passwordEncoder.encode("Leader123!"))
                    .active(true)
                    .role(leaderRole)
                    .build());
        }

        if (!userRepository.existsByUsername("employee")) {
            userRepository.save(User.builder()
                    .username("employee")
                    .email("employee@example.com")
                    .password(passwordEncoder.encode("Employee123!"))
                    .active(true)
                    .role(employeeRole)
                    .build());
        }
    }
}