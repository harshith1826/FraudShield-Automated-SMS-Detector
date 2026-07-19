package com.fraudshield.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sms_analysis")
public class SmsAnalysis {

    @Id
    private String id;

    @Field("message")
    private String message;

    @Field("sender")
    private String sender;

    @Field("prediction")
    private String prediction;

    @Field("confidence")
    private double confidence;

    @Field("riskScore")
    private int riskScore;

    @Field("threatLevel")
    private String threatLevel;

    @Field("reason")
    private String reason;

    @Field("containsUrl")
    private boolean containsUrl;

    @Field("extractedUrls")
    private List<String> extractedUrls;

    @Field("analyzedAt")
    private LocalDateTime analyzedAt;

    @Field("fraudScore")
    private double fraudScore;

    @Field("category")
    private String category;

    @Field("action")
    private String action;

    @Field("reasons")
    private List<String> reasons;

    @Field("signals")
    private Map<String, Object> signals;

    @Field("primaryUrl")
    private String primaryUrl;

    @Field("warningTextEnglish")
    private String warningTextEnglish;

    @Field("warningTextHindi")
    private String warningTextHindi;

    @Field("warningTextTamil")
    private String warningTextTamil;

    @Field("warningTextTelugu")
    private String warningTextTelugu;

    @Field("warningTextKannada")
    private String warningTextKannada;

    @Field("campaignDetected")
    private boolean campaignDetected;

    @Field("specificThreat")
    private String specificThreat;

    @Field("deviceId")
    private String deviceId;
}