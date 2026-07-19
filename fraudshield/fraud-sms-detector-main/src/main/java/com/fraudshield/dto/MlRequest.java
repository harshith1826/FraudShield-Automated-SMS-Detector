package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MlRequest {

    @JsonProperty("url")
    private String url;

    @JsonProperty("sender_id")
    private String senderId;

    @JsonProperty("sms_body")
    private String smsBody;

    @JsonProperty("timestamp")
    private long timestamp;
}