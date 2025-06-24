package com.epam.javadvanced.security.task3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("task3")
public class BruteForceService {

    private final LoginAttemptRepository loginAttemptRepository;

    @Value("${security.brute-force.max-attempts:3}")
    private int maxAttempts;

    @Value("${security.brute-force.lock-duration-minutes:5}")
    private int lockDurationMinutes;

    public boolean isBlocked(String email) {
        LocalDateTime now = LocalDateTime.now();
        
        // Check if user is currently blocked
        LoginAttempt blockedAttempt = loginAttemptRepository.findTopByEmailAndBlockedUntilAfterOrderByAttemptTimeDesc(email, now);
        if (blockedAttempt != null) {
            log.warn("User {} is blocked until {}", email, blockedAttempt.getBlockedUntil());
            return true;
        }

        return false;
    }

    public void recordSuccessfulLogin(String email) {
        LoginAttempt attempt = new LoginAttempt(email, true);
        loginAttemptRepository.save(attempt);
        log.info("Successful login recorded for {}", email);
    }

    public void recordFailedLogin(String email) {
        LoginAttempt attempt = new LoginAttempt(email, false);
        loginAttemptRepository.save(attempt);

        // Check recent failed attempts
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        List<LoginAttempt> recentFailedAttempts = loginAttemptRepository
            .findByEmailAndSuccessfulFalseAndAttemptTimeAfterOrderByAttemptTimeDesc(email, fiveMinutesAgo);

        if (recentFailedAttempts.size() >= maxAttempts) {
            // Block the user
            LocalDateTime blockedUntil = LocalDateTime.now().plusMinutes(lockDurationMinutes);
            attempt.setBlockedUntil(blockedUntil);
            loginAttemptRepository.save(attempt);
            
            log.warn("User {} blocked until {} after {} failed attempts", email, blockedUntil, recentFailedAttempts.size());
        }
    }

    public List<LoginAttempt> getBlockedUsers() {
        LocalDateTime now = LocalDateTime.now();
        return loginAttemptRepository.findByBlockedUntilAfterOrderByAttemptTimeDesc(now);
    }
}