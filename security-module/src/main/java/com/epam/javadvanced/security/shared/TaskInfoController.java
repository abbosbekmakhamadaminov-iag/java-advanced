package com.epam.javadvanced.security.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TaskInfoController {

    private final Environment environment;

    @Value("${info.task.number:0}")
    private int taskNumber;

    @Value("${info.task.description:Unknown}")
    private String taskDescription;

    @Value("${info.task.points:0}")
    private int taskPoints;

    public TaskInfoController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/task-info")
    public Map<String, Object> getTaskInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("currentTask", taskNumber);
        info.put("description", taskDescription);
        info.put("points", taskPoints);
        info.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        info.put("availableTasks", Arrays.asList(
            "task1 - Basic Security Setup (20 points)",
            "task2 - Database & Password Security (20 points)", 
            "task3 - Advanced Security (40 points)",
            "task4 - Secret Sharing Application (20 points)"
        ));
        info.put("usage", "Run with: --spring.profiles.active=task1|task2|task3|task4");
        return info;
    }
}