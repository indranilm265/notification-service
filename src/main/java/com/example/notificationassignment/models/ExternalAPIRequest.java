package com.example.notificationassignment.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalAPIRequest {

    @JsonProperty("deliverychannel")
    String deliveryChannel;

    @JsonProperty("channels")
    Channels channels;

    @JsonProperty("destination")
    List<Destination> destinations;

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String formatted = "";
        try {
            formatted = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return formatted;
    }
}
