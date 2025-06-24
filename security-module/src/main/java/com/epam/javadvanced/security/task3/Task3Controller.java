package com.epam.javadvanced.security.task3;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@Profile("task3")
public class Task3Controller {

    private final BruteForceService bruteForceService;
    private final Random random = new Random();
    
    private final String[] infoStats = {
        "Advanced security system", 
        "Permission-based access control",
        "Brute force protection enabled",
        "Custom login/logout pages",
        "Role-based authorization"
    };

    private final String[] adminStats = {
        "Admin dashboard active", 
        "System monitoring enabled",
        "User management portal",
        "Security analytics",
        "Administrative controls"
    };

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('VIEW_INFO')")
    public Map<String, Object> getInfo(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", infoStats[random.nextInt(infoStats.length)]);
        response.put("timestamp", System.currentTimeMillis());
        response.put("task", "Task 3 - Advanced Security");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities());
        return response;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('VIEW_ADMIN')")
    public Map<String, Object> getAdmin(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", adminStats[random.nextInt(adminStats.length)]);
        response.put("timestamp", System.currentTimeMillis());
        response.put("task", "Task 3 - Admin Endpoint");
        response.put("user", auth.getName());
        response.put("authorities", auth.getAuthorities());
        response.put("access", "Admin permission required");
        return response;
    }

    @GetMapping("/about")
    public Map<String, Object> getAbout() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Security Homework Task 3");
        response.put("description", "Advanced security with permissions and brute force protection");
        response.put("features", new String[]{
            "Permission-based access control",
            "Custom login/logout pages", 
            "Brute force protection",
            "Blocked users tracking"
        });
        response.put("access", "public - no authentication required");
        return response;
    }

    @GetMapping("/blocked-users")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getBlockedUsers() {
        List<LoginAttempt> blockedUsers = bruteForceService.getBlockedUsers();
        
        Map<String, Object> response = new HashMap<>();
        response.put("blockedUsers", blockedUsers);
        response.put("count", blockedUsers.size());
        response.put("description", "Users currently blocked due to failed login attempts");
        return response;
    }
}