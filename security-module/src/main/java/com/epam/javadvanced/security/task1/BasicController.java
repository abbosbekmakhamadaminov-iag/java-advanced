package com.epam.javadvanced.security.task1;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@Profile("task1")
public class BasicController {

    private final Random random = new Random();
    
    private final String[] stats = {
        "MVC application", 
        "Spring Boot is awesome",
        "Security is important",
        "Random stat generator",
        "Java 17 powered"
    };

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", stats[random.nextInt(stats.length)]);
        response.put("timestamp", System.currentTimeMillis());
        response.put("task", "Task 1 - Basic Security Setup");
        return response;
    }
}