package com.example.notificationassignment.listeners;

import com.example.notificationassignment.models.ExternalAPIRequest;
import com.example.notificationassignment.models.SmsRequest;
import com.example.notificationassignment.repository.SmsRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.notificationassignment.utils.GenerateExternalAPIRequest;

import java.io.IOException;
import java.util.Date;

@Service
public class KafkaConsumer {
    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final SmsRequestRepository smsRequestRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public KafkaConsumer(SmsRequestRepository smsRequestRepository, RestTemplate restTemplate) {
        this.smsRequestRepository = smsRequestRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = {"mytopic"}, groupId = "myGroup")
    public void consume(String message) throws IOException{
        logger.info("Consumed message : {}", message);
        long id = Long.parseLong(message);
        SmsRequest smsRequest = smsRequestRepository.getSmsRequestById(id);
        Date date = new Date();
        smsRequest.setUpdatedAt(date);
        smsRequest.setStatus("sms received in kafka consumer");
        HttpEntity<ExternalAPIRequest> httpEntity = GenerateExternalAPIRequest.generateExternalAPIRequest(smsRequest);
        String URI = "https://api.imiconnect.in/resources/v1/messaging";
        logger.info(URI);
        // TODO: use reactive
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URI, httpEntity, String.class);
        HttpStatus status = responseEntity.getStatusCode();
        String restCall = responseEntity.getBody();
        logger.info(String.valueOf(status));
        logger.info(restCall);
    }
}
