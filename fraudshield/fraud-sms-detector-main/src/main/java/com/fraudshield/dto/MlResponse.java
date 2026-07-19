package com.fraudshield.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MlResponse {

    @JsonProperty("url")
    private String url;

    @JsonProperty("sender_id")
    private String senderId;

    @JsonProperty("verdict")
    private String verdict;

    @JsonProperty("confidence")
    private double confidence;

    @JsonProperty("fraud_score")
    private double fraudScore;

    @JsonProperty("action")
    private String action;

    @JsonProperty("category")
    private String category;

    @JsonProperty("reasons")
    private List<String> reasons;

    @JsonProperty("signals")
    private Map<String, Object> signals;

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

    @JsonProperty("redirect_hops")
    private int redirectHops;

    @JsonProperty("final_url")
    private String finalUrl;

    @JsonProperty("form_fields_collected")
    private List<String> formFieldsCollected;

    @JsonProperty("critical_fields_found")
    private List<String> criticalFieldsFound;
}