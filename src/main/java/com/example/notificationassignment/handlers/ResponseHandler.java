package com.example.notificationassignment.handlers;

import com.example.notificationassignment.models.SmsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    static Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    public static ResponseEntity<?> generateSuccessResponse(long id) {

        logger.info("In success handler", id);

        Map<String, Map<String, String>> successResponse = new HashMap<String, Map<String, String>>();

        Map <String, String> data = new HashMap<>();
        data.put("request_id", String.valueOf(id));
        data.put("comments", "Successfully sent");
        successResponse.put("data", data);
        return new ResponseEntity(successResponse, HttpStatus.OK);
    }

    public static ResponseEntity<?> generateFailureResponse(String phoneNumber){

        Map<String, Map<String, String>> failureResponse = new HashMap<String, Map<String, String>>();

        Map <String, String> error = new HashMap<>();
        error.put("code", "INVALID_REQUEST");
        if(phoneNumber.equals("")){
            error.put("message", "phone_number is mandatory");
        }
        else {
            error.put("message", String.format("%s is blacklisted", phoneNumber));
        }
        failureResponse.put("error", error);
        return new ResponseEntity(failureResponse, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> generateSuccessBlacklistResponse(){
        Map <String, String> successResponse = new HashMap<>();
        successResponse.put("data", "Successfully Blacklisted");
        return new ResponseEntity(successResponse, HttpStatus.OK);
    }

    public static ResponseEntity<SmsRequest> generateFetchSmsSuccessResponse(SmsRequest smsRequest){
        return new ResponseEntity(smsRequest, HttpStatus.OK);
    }

    public static ResponseEntity<?> generateFetchSmsRequestFailureResponse(){
        Map<String, Map<String, String>> failureResponse = new HashMap<String, Map<String, String>>();

        Map <String, String> error = new HashMap<>();
        error.put("code", "INVALID_REQUEST");
        error.put("message", "request_id not found");
        failureResponse.put("error", error);
        return new ResponseEntity(failureResponse, HttpStatus.BAD_REQUEST);
    }
}
