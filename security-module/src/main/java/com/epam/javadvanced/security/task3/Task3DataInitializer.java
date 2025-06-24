package com.epam.javadvanced.security.task3;

import com.epam.javadvanced.security.task2.PasswordService;
import com.epam.javadvanced.security.task2.User;
import com.epam.javadvanced.security.task2.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("task3")
public class Task3DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final PasswordService passwordService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing Task 3 data with permissions");
            
            // Create permissions
            Permission viewInfo = createPermission("VIEW_INFO", "Can view info endpoint");
            Permission viewAdmin = createPermission("VIEW_ADMIN", "Can view admin endpoint");
            
            // Create users with different permission combinations
            User user1 = createUser("user1@example.com", "password", "USER");
            User user2 = createUser("user2@example.com", "password", "USER");
            User admin = createUser("admin@example.com", "admin123", "ADMIN");
            
            // Assign permissions
            // User1: Only VIEW_INFO
            createUserPermission(user1, viewInfo);
            
            // User2: Only VIEW_ADMIN
            createUserPermission(user2, viewAdmin);
            
            // Admin: Both permissions
            createUserPermission(admin, viewInfo);
            createUserPermission(admin, viewAdmin);
            
            log.info("Created {} users with permissions", userRepository.count());
        }
    }

    private Permission createPermission(String name, String description) {
        Permission permission = new Permission(name, description);
        return permissionRepository.save(permission);
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