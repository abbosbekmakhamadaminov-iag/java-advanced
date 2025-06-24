package com.epam.javadvanced.security.task4;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Profile("task4")
public class Task4Controller {

    private final SecretService secretService;

    @PostMapping("/secret")
    @PreAuthorize("hasAuthority('STANDARD')")
    public Map<String, Object> createSecret(@RequestBody Map<String, String> request, Authentication auth) {
        String content = request.get("content");
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret content cannot be empty");
        }

        String secretLink = secretService.createSecret(content, auth.getName());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Secret created successfully");
        response.put("link", secretLink);
        response.put("sender", auth.getName());
        response.put("note", "This link can only be accessed once and will be destroyed after viewing");
        return response;
    }

    @GetMapping("/secret/{uniqueLink}")
    public Map<String, Object> getSecret(@PathVariable String uniqueLink, Authentication auth) {
        // Check if user has STANDARD permission
        boolean hasStandardPermission = auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("STANDARD"));
        
        if (!hasStandardPermission) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Access denied. STANDARD permission required.");
            return response;
        }

        Optional<String> secretContent = secretService.getSecretByLink(uniqueLink);
        
        Map<String, Object> response = new HashMap<>();
        if (secretContent.isPresent()) {
            response.put("content", secretContent.get());
            response.put("message", "Secret retrieved successfully. This message has been destroyed.");
            response.put("recipient", auth.getName());
        } else {
            response.put("error", "Secret not found, already accessed, or expired");
        }
        
        return response;
    }

    @GetMapping("/secret-form")
    @PreAuthorize("hasAuthority('STANDARD')")
    public String getSecretForm() {
        return "secret-form";
    }

    @GetMapping("/about")
    public Map<String, Object> getAbout() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Secret Providers - Task 4");
        response.put("description", "One-time secret sharing with unique links");
        response.put("features", new String[]{
            "One-time secret access",
            "Unique link generation", 
            "STANDARD permission required",
            "Automatic cleanup after access"
        });
        response.put("access", "public - no authentication required");
        return response;
    }

    @PostMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> cleanupSecrets() {
        secretService.cleanupExpiredSecrets();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Expired secrets cleaned up successfully");
        return response;
    }
}