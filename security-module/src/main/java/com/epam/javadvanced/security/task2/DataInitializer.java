package com.epam.javadvanced.security.task2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile({"task2", "task3", "task4"})
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing users for Task 2+");
            
            // Create test users with salted passwords
            createUser("user@example.com", "password");
            createUser("admin@example.com", "admin123");
            createUser("test@example.com", "test123");
            
            log.info("Created {} users", userRepository.count());
        }
    }

    private void createUser(String email, String password) {
        String salt = passwordService.generateSalt();
        String hashedPassword = passwordService.hashPassword(password, salt);
        
        User user = new User(email, hashedPassword, salt);
        if (email.contains("admin")) {
            user.setRole("ADMIN");
        }
        
        userRepository.save(user);
        log.info("Created user: {} with role: {}", email, user.getRole());
    }
}