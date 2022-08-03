package com.example.notificationassignment.controller;

import com.example.notificationassignment.config.KafkaProducerConfig;
import com.example.notificationassignment.handlers.ResponseHandler;
import com.example.notificationassignment.models.BlacklistPhoneNumber;
import com.example.notificationassignment.models.BlacklistPhoneNumbersRequest;
import com.example.notificationassignment.models.SmsRequest;
import com.example.notificationassignment.repository.BlacklistRepository;
import com.example.notificationassignment.repository.SmsRequestRepository;
import com.example.notificationassignment.utils.PostSmsRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class SmsRequestController {

    Logger logger = LoggerFactory.getLogger(SmsRequestController.class);
    private final SmsRequestRepository smsRequestRepository;
    private final KafkaProducerConfig kafkaProducerConfig;
    private final BlacklistRepository blacklistRepository;
    private final RedisTemplate<String, BlacklistPhoneNumber> redisTemplate;
    private final SetOperations setOperations;

    private final PostSmsRequestUtil postSmsRequestUtil;

    @Autowired
    public SmsRequestController(SmsRequestRepository smsRequestRepository, KafkaProducerConfig kafkaProducerConfig, BlacklistRepository blacklistRepository, RedisTemplate<String, BlacklistPhoneNumber> redisTemplate, PostSmsRequestUtil postSmsRequestUtil) {
        this.kafkaProducerConfig = kafkaProducerConfig;
        this.redisTemplate = redisTemplate;
        this.setOperations = redisTemplate.opsForSet();
        this.blacklistRepository = blacklistRepository;
        this.smsRequestRepository = smsRequestRepository;
        this.postSmsRequestUtil = postSmsRequestUtil;
    }

    @GetMapping("/sms")
    public List<SmsRequest> getAllSmsRequests(){
        List<SmsRequest> smsRequests = new ArrayList<>();
        smsRequestRepository.findAll().forEach(smsRequests::add);
        return smsRequests;
    }

    @GetMapping("/sms/{requestId}")
    public ResponseEntity<?> getSmsRequestByRequestId(@PathVariable long requestId){
        boolean smsRequestExists = smsRequestRepository.existsById(requestId);
        if(smsRequestExists){
            SmsRequest smsRequest = smsRequestRepository.getSmsRequestById(requestId);
            return ResponseHandler.generateFetchSmsSuccessResponse(smsRequest);
        }
        return ResponseHandler.generateFetchSmsRequestFailureResponse();
    }

    @PostMapping("/sms/send")
    public ResponseEntity<?> postSmsRequest(@RequestBody SmsRequest newSmsRequest){
        return postSmsRequestUtil.postSmsRequestUtil(newSmsRequest);
    }

}
