package com.epam.javadvanced.security.task4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SecretRepository extends JpaRepository<Secret, Long> {
    Optional<Secret> findByUniqueLink(String uniqueLink);
    List<Secret> findByExpiresAtBeforeOrAccessedTrue(LocalDateTime now);
    void deleteByExpiresAtBeforeOrAccessedTrue(LocalDateTime now);
}