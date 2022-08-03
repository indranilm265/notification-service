package com.example.notificationassignment.utils;

import com.example.notificationassignment.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

public class GenerateExternalAPIRequest {
    static Logger logger = LoggerFactory.getLogger(GenerateExternalAPIRequest.class);
    public static HttpEntity<ExternalAPIRequest> generateExternalAPIRequest(SmsRequest smsRequest){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type"," application/json");
        headers.add("key", "93ceffda-5941-11ea-9da9-025282c394f2");
        Sms sms = new Sms(smsRequest.getMessage());
        Channels channels = new Channels(sms);
        List<Destination> destination = new ArrayList<>();
        List<String> msisdn = new ArrayList<>();
        msisdn.add(smsRequest.getPhoneNumber());
        destination.add(new Destination(msisdn, String.valueOf(smsRequest.getId())));
        ExternalAPIRequest externalAPIRequest = new ExternalAPIRequest("sms", channels, destination);
        logger.info(externalAPIRequest.toString());
        return new HttpEntity<>(externalAPIRequest, headers);
    }
}
