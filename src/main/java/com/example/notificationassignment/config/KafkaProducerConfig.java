package com.example.notificationassignment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerConfig {

    Logger logger = LoggerFactory.getLogger(KafkaProducerConfig.class);
    private static final String topic = "mytopic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducerConfig(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishToTopic(String message){
        logger.info("Sending message : {}", message);
        this.kafkaTemplate.send(topic, message);
    }
}
