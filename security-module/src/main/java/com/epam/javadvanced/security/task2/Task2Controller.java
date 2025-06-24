package com.epam.javadvanced.security.task2;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@Profile("task2")
public class Task2Controller {

    private final Random random = new Random();
    
    private final String[] stats = {
        "Database-backed MVC application", 
        "Password hashing with salt",
        "Multi-user security system",
        "H2 database integration",
        "BCrypt password encoder"
    };

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", stats[random.nextInt(stats.length)]);
        response.put("timestamp", System.currentTimeMillis());
        response.put("task", "Task 2 - Database & Password Security");
        return response;
    }

    @GetMapping("/about")
    public Map<String, Object> getAbout() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Security Homework Task 2");
        response.put("description", "Demonstrates database user storage with salted password hashing");
        response.put("features", new String[]{
            "Database user storage",
            "Salt-based password hashing", 
            "Public endpoint access",
            "H2 console enabled"
        });
        response.put("access", "public - no authentication required");
        return response;
    }
}