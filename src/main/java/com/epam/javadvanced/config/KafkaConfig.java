package com.epam.javadvanced.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${app.kafka.topics.product-events}")
    private String productEvents;

    @Value("${app.kafka.topics.customer-events}")
    private String customerEvents;

    @Bean
    public NewTopic productEventsTopic() {
        return TopicBuilder
                .name(productEvents)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic customerEventsTopic() {
        return TopicBuilder
                .name(customerEvents)
                .partitions(3)
                .replicas(1)
                .build();
    }


}
