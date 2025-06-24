package com.epam.javadvanced.security.task3;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Profile("task3")
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final BruteForceService bruteForceService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
        
        String email = request.getParameter("username");
        if (email != null) {
            bruteForceService.recordFailedLogin(email);
        }
        
        response.sendRedirect("/login?error");
    }
}