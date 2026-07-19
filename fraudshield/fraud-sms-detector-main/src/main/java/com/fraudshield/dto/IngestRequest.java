package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IngestRequest {

    @NotBlank(message = "sender_id must not be blank")
    @JsonProperty("sender_id")
    private String senderId;

    @NotBlank(message = "sms_body must not be blank")
    @JsonProperty("sms_body")
    private String smsBody;

    @JsonProperty("extracted_url")
    private String extractedUrl;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("device_id")
    private String deviceId;
}