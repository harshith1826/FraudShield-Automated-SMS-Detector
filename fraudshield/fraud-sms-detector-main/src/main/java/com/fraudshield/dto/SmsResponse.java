package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsResponse {

    private String prediction;
    private double confidence;
    private int riskScore;
    private String threatLevel;
    private String reason;
    private String analyzedAt;
}