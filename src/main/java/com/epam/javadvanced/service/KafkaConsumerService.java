package com.epam.javadvanced.service;

import com.epam.javadvanced.avro.CustomerEvent;
import com.epam.javadvanced.avro.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {


    @KafkaListener(topics = "${app.kafka.topics.product-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeProductEvent(@Payload ProductEvent productEvent) {


        log.info("Received ProductEvent: {}", productEvent);
    }

    @KafkaListener(topics = "${app.kafka.topics.customer-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeCustomerEvent(@Payload CustomerEvent customerEvent) {


        log.info("Received CustomerEvent: {}", customerEvent);
    }


}
