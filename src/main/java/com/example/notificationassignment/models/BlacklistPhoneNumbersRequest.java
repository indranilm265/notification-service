package com.example.notificationassignment.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistPhoneNumbersRequest {
    @JsonProperty("phone_numbers")
    private String[] phoneNumbers;
}
