package com.example.notificationassignment.utils;

import com.example.notificationassignment.config.KafkaProducerConfig;
import com.example.notificationassignment.handlers.ResponseHandler;
import com.example.notificationassignment.models.BlacklistPhoneNumber;
import com.example.notificationassignment.models.SmsRequest;
import com.example.notificationassignment.repository.BlacklistRepository;
import com.example.notificationassignment.repository.SmsRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PostSmsRequestUtil {

    static Logger logger = LoggerFactory.getLogger(PostSmsRequestUtil.class);

    private final SmsRequestRepository smsRequestRepository;
    private final KafkaProducerConfig kafkaProducerConfig;
    private final BlacklistRepository blacklistRepository;
    private final RedisTemplate<String, BlacklistPhoneNumber> redisTemplate;
    private final SetOperations setOperations;

    @Autowired
    public PostSmsRequestUtil(SmsRequestRepository smsRequestRepository, KafkaProducerConfig kafkaProducerConfig, BlacklistRepository blacklistRepository, RedisTemplate<String, BlacklistPhoneNumber> redisTemplate) {
        this.smsRequestRepository = smsRequestRepository;
        this.kafkaProducerConfig = kafkaProducerConfig;
        this.blacklistRepository = blacklistRepository;
        this.redisTemplate = redisTemplate;
        this.setOperations = redisTemplate.opsForSet();
    }

    public ResponseEntity<?> postSmsRequestUtil(SmsRequest newSmsRequest){
        String phoneNumber = newSmsRequest.getPhoneNumber();
        // TODO: check phone number not null here
        logger.info("Phone Number received : {}", phoneNumber);
        Date date = new Date();
        newSmsRequest.setStatus("sms request received in server");
        newSmsRequest.setCreatedAt(date);
        newSmsRequest.setUpdatedAt(date);
        boolean isPhoneNumberInBlacklistCache = Boolean.TRUE.equals(redisTemplate.hasKey(phoneNumber));
        boolean isPhoneNumberInBlacklist = isPhoneNumberInBlacklistCache;
        if(!isPhoneNumberInBlacklistCache){
            isPhoneNumberInBlacklist = blacklistRepository.existsByPhoneNumber(phoneNumber);
            if(isPhoneNumberInBlacklist){
                BlacklistPhoneNumber blacklistPhoneNumber = new BlacklistPhoneNumber();
                blacklistPhoneNumber.setPhoneNumber(phoneNumber);
                setOperations.add(phoneNumber, blacklistPhoneNumber);
            }
        }
        if(!phoneNumber.equals("") && !isPhoneNumberInBlacklist){
            smsRequestRepository.save(newSmsRequest);
            long id = newSmsRequest.getId();
            kafkaProducerConfig.publishToTopic(String.valueOf(id));
            return ResponseHandler.generateSuccessResponse(id);
        }
        return ResponseHandler.generateFailureResponse(phoneNumber);
    }
}
