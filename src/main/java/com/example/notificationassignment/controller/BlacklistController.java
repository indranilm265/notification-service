package com.example.notificationassignment.controller;

import com.example.notificationassignment.config.KafkaProducerConfig;
import com.example.notificationassignment.handlers.ResponseHandler;
import com.example.notificationassignment.models.BlacklistPhoneNumber;
import com.example.notificationassignment.models.BlacklistPhoneNumbersRequest;
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
public class BlacklistController {
    Logger logger = LoggerFactory.getLogger(BlacklistController.class);

    private final SmsRequestRepository smsRequestRepository;
    private final KafkaProducerConfig kafkaProducerConfig;
    private final BlacklistRepository blacklistRepository;
    private final RedisTemplate<String, BlacklistPhoneNumber> redisTemplate;
    private final SetOperations setOperations;

    private final PostSmsRequestUtil postSmsRequestUtil;

    @Autowired
    public BlacklistController(SmsRequestRepository smsRequestRepository, KafkaProducerConfig kafkaProducerConfig, BlacklistRepository blacklistRepository, RedisTemplate<String, BlacklistPhoneNumber> redisTemplate, PostSmsRequestUtil postSmsRequestUtil) {
        this.smsRequestRepository = smsRequestRepository;
        this.kafkaProducerConfig = kafkaProducerConfig;
        this.blacklistRepository = blacklistRepository;
        this.redisTemplate = redisTemplate;
        this.setOperations = redisTemplate.opsForSet();
        this.postSmsRequestUtil = postSmsRequestUtil;
    }

    @GetMapping("/blacklist")
    public List<BlacklistPhoneNumber> getAllBlacklistNumbers(){
        List<BlacklistPhoneNumber> blacklistPhoneNumbers = new ArrayList<>();
        blacklistRepository.findAll().forEach(blacklistPhoneNumbers::add);
        return blacklistPhoneNumbers;
    }


    @PostMapping("/blacklist")
    public ResponseEntity<?> postBlacklist(@RequestHeader String Auth, @RequestBody BlacklistPhoneNumbersRequest blacklistPhoneNumbersRequest){
        String[] phoneNumbers = blacklistPhoneNumbersRequest.getPhoneNumbers();
        for(String phoneNumber : phoneNumbers){
            BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber();
            blacklistPhoneNumber.setPhoneNumber(phoneNumber);
            blacklistRepository.save(blacklistPhoneNumber);
            setOperations.add(phoneNumber, blacklistPhoneNumber);
        }
        return ResponseHandler.generateSuccessBlacklistResponse();
    }

    @DeleteMapping("/blacklist/remove/{phoneNumber}")
    public void deletePhoneNumberFromBlacklist(@PathVariable String phoneNumber){
        BlacklistPhoneNumber blacklistPhoneNumber = blacklistRepository.getBlacklistPhoneNumberByPhoneNumber(phoneNumber);
        logger.info(blacklistPhoneNumber.toString());
        blacklistRepository.delete(blacklistPhoneNumber);
        setOperations.remove(phoneNumber, blacklistPhoneNumber);
    }
}
