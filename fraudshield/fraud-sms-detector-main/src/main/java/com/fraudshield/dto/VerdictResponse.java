package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerdictResponse {

    @JsonProperty("url")
    private String url;

    @JsonProperty("verdict")
    private String verdict;

    @JsonProperty("confidence")
    private double confidence;

    @JsonProperty("category")
    private String category;

    @JsonProperty("warning_text_english")
    private String warningTextEnglish;

    @JsonProperty("warning_text_hindi")
    private String warningTextHindi;

    @JsonProperty("warning_text_tamil")
    private String warningTextTamil;

    @JsonProperty("warning_text_telugu")
    private String warningTextTelugu;

    @JsonProperty("warning_text_kannada")
    private String warningTextKannada;

    @JsonProperty("campaign_detected")
    private boolean campaignDetected;

    @JsonProperty("specific_threat")
    private String specificThreat;

    @JsonProperty("action")
    private String action;
}