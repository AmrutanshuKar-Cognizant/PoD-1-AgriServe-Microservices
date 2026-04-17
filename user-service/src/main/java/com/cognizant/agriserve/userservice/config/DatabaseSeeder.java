package com.cognizant.agriserve.userservice.config;

import com.cognizant.agriserve.userservice.entity.User;
import com.cognizant.agriserve.userservice.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Database is empty. Seeding dummy users...");

            List<User> dummyUsers = List.of(
                    createUser("Admin User", "admin@agriserve.com", "9000000001", "Admin"),
                    createUser("John Officer", "extension@agriserve.com", "9000000002", "ExtensionOfficer"),
                    createUser("Sarah Compliance", "compliance@agriserve.com", "9000000003", "ComplianceOfficer"),
                    createUser("Mike Auditor", "auditor@agriserve.com", "9000000004", "Auditor"),
                    createUser("Emily Manager", "manager@agriserve.com", "9000000005", "ProgramManager")
            );

            userRepository.saveAll(dummyUsers);
            log.info("Successfully seeded {} users into the database.", dummyUsers.size());
        } else {
            log.info("User table already contains data. Skipping seeding.");
        }
    }

    private User createUser(String name, String email, String phone, String role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(User.Role.valueOf(role));
        // All dummy users get the same default password for testing
        user.setPassword(passwordEncoder.encode("Password@123"));
        return user;
    }
}