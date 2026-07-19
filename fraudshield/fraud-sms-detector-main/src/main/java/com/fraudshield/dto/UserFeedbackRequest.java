package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserFeedbackRequest {

    @NotBlank(message = "url must not be blank")
    @JsonProperty("url")
    private String url;

    @NotNull(message = "user_signal must not be null")
    @JsonProperty("user_signal")
    private Integer userSignal;

    @NotBlank(message = "current_system_verdict must not be blank")
    @JsonProperty("current_system_verdict")
    private String currentSystemVerdict;

    @JsonProperty("user_age_group")
    private String userAgeGroup;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("timestamp")
    private long timestamp;
}