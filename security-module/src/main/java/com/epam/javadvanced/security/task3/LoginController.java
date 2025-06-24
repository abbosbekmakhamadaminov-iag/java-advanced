package com.epam.javadvanced.security.task3;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("task3")
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}