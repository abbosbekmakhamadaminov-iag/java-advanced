package com.epam.javadvanced.security.task4;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("task4")
public class SecretService {

    private final SecretRepository secretRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${secrets.base-url:http://localhost:8084}")
    private String baseUrl;

    public String createSecret(String content, String senderEmail) {
        String uniqueLink = generateUniqueLink();
        Secret secret = new Secret(uniqueLink, content, senderEmail);
        secretRepository.save(secret);
        
        String fullUrl = baseUrl + "/secret/" + uniqueLink;
        log.info("Created secret with link: {} for sender: {}", uniqueLink, senderEmail);
        return fullUrl;
    }

    @Transactional
    public Optional<String> getSecretByLink(String uniqueLink) {
        Optional<Secret> secretOpt = secretRepository.findByUniqueLink(uniqueLink);
        
        if (secretOpt.isEmpty()) {
            log.warn("Secret not found for link: {}", uniqueLink);
            return Optional.empty();
        }

        Secret secret = secretOpt.get();
        
        // Check if already accessed
        if (secret.isAccessed()) {
            log.warn("Secret already accessed for link: {}", uniqueLink);
            return Optional.empty();
        }
        
        // Check if expired
        if (secret.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Secret expired for link: {}", uniqueLink);
            secretRepository.delete(secret);
            return Optional.empty();
        }

        // Mark as accessed and delete
        secret.setAccessed(true);
        secret.setAccessedAt(LocalDateTime.now());
        secretRepository.save(secret);
        
        // Delete the secret immediately after access
        secretRepository.delete(secret);
        
        log.info("Secret accessed and deleted for link: {}", uniqueLink);
        return Optional.of(secret.getContent());
    }

    @Transactional
    public void cleanupExpiredSecrets() {
        LocalDateTime now = LocalDateTime.now();
        secretRepository.deleteByExpiresAtBeforeOrAccessedTrue(now);
        log.info("Cleaned up expired and accessed secrets");
    }

    private String generateUniqueLink() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}