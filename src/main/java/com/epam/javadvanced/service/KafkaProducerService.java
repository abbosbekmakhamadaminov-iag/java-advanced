package com.epam.javadvanced.service;

import com.epam.javadvanced.avro.CustomerEvent;
import com.epam.javadvanced.avro.EventType;
import com.epam.javadvanced.avro.ProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.product-events}")
    private String productEventsTopic;

    @Value("${app.kafka.topics.customer-events}")
    private String customerEventsTopic;

    public void sendProductEvent(Long productId, String productName, Double price, EventType eventType) {
        ProductEvent productEvent = ProductEvent.newBuilder()
                .setId(productId)
                .setName(productName)
                .setPrice(price)
                .setEventType(eventType)
                .setTimestamp(System.currentTimeMillis())
                .build();

        String key = "product-" + productId;
        log.info("Sending ProductEvent: {} for {}", eventType, key);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(productEventsTopic, key, productEvent);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("ProductEvent sent successfully: {} offset: {}", eventType, result.getRecordMetadata().offset());
            } else {
                log.error("Exception: event {}", eventType, exception);
            }
        });
    }

    public void sendCustomerEvent(Long customerId, String firstName, String lastName, Double balance, EventType eventType) {
        CustomerEvent customerEvent = CustomerEvent.newBuilder()
                .setId(customerId)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setBalance(balance)
                .setEventType(eventType)
                .setTimestamp(System.currentTimeMillis())
                .build();

        String key = "customer-" + customerId;

        log.info("Sending CustomerEvent: {} for customer ID: {}", eventType, customerId);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(customerEventsTopic, key, customerEvent);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("CustomerEvent sent successfully: {} with offset: {}",
                        eventType, result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send CustomerEvent: {}", eventType, exception);
            }
        });
    }
}
