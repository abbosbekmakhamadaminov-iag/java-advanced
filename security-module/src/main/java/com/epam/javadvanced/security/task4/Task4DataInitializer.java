package com.epam.javadvanced.security.task4;

import com.epam.javadvanced.security.task2.PasswordService;
import com.epam.javadvanced.security.task2.User;
import com.epam.javadvanced.security.task2.UserRepository;
import com.epam.javadvanced.security.task3.Permission;
import com.epam.javadvanced.security.task3.PermissionRepository;
import com.epam.javadvanced.security.task3.UserPermission;
import com.epam.javadvanced.security.task3.UserPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("task4")
public class Task4DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final PasswordService passwordService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing Task 4 data with STANDARD permission");
            
            // Create STANDARD permission
            Permission standardPermission = createPermission("STANDARD", "Can use secret sharing features");
            
            // Create users with STANDARD permission
            User sender = createUser("sender@example.com", "password", "USER");
            User recipient = createUser("recipient@example.com", "password", "USER");
            User admin = createUser("admin@example.com", "admin123", "ADMIN");
            
            // Assign STANDARD permission to all users
            createUserPermission(sender, standardPermission);
            createUserPermission(recipient, standardPermission);
            createUserPermission(admin, standardPermission);
            
            log.info("Created {} users with STANDARD permission for secret sharing", userRepository.count());
        }
    }

    private Permission createPermission(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(new Permission(name, description)));
    }

    private User createUser(String email, String password, String role) {
        String salt = passwordService.generateSalt();
        String hashedPassword = passwordService.hashPassword(password, salt);
        
        User user = new User(email, hashedPassword, salt);
        user.setRole(role);
        return userRepository.save(user);
    }

    private UserPermission createUserPermission(User user, Permission permission) {
        UserPermission userPermission = new UserPermission(user, permission);
        return userPermissionRepository.save(userPermission);
    }
}