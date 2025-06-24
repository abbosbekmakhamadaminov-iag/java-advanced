package com.epam.javadvanced.security.task3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    
    List<LoginAttempt> findByEmailAndSuccessfulFalseAndAttemptTimeAfterOrderByAttemptTimeDesc(
        String email, LocalDateTime after);
    
    LoginAttempt findTopByEmailAndBlockedUntilAfterOrderByAttemptTimeDesc(
        String email, LocalDateTime now);
    
    List<LoginAttempt> findByBlockedUntilAfterOrderByAttemptTimeDesc(LocalDateTime now);
}