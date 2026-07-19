package com.fraudshield.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraudshield.dto.MlResponse;
import com.fraudshield.exception.AiServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class AiPredictionService {

    private final RestTemplate restTemplate;
    private final String mlAnalyseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiPredictionService(RestTemplate restTemplate,
                                @Value("${fastapi.base-url}") String fastApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.mlAnalyseUrl = fastApiBaseUrl + "/ml/analyse";
    }

    public MlResponse predict(String smsBody, String senderId, List<String> extractedUrls) {
        log.info("Calling FastAPI ML service at: {}", mlAnalyseUrl);

        String primaryUrl = (extractedUrls != null && !extractedUrls.isEmpty())
                ? extractedUrls.get(0)
                : "http://unknown.com";

        String jsonBody = String.format(
                "{\"url\":\"%s\",\"sender_id\":\"%s\",\"sms_body\":\"%s\",\"timestamp\":%d}",
                primaryUrl.replace("\"", "\\\""),
                senderId.replace("\"", "\\\""),
                smsBody.replace("\"", "\\\"").replace("\n", " "),
                Instant.now().getEpochSecond()
        );

        log.info("Sending raw JSON to ML: {}", jsonBody);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Accept", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<String> rawResponse = restTemplate.postForEntity(
                    mlAnalyseUrl, requestEntity, String.class
            );

            log.info("Raw ML response: {}", rawResponse.getBody());

            MlResponse mlResponse = objectMapper.readValue(rawResponse.getBody(), MlResponse.class);

            if (mlResponse == null) {
                throw new AiServiceException("Empty response received from ML service");
            }

            log.info("ML verdict: {}, confidence: {}, fraudScore: {}",
                    mlResponse.getVerdict(), mlResponse.getConfidence(), mlResponse.getFraudScore());

            return mlResponse;

        } catch (AiServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Failed to call ML service: {}", ex.getMessage());
            throw new AiServiceException("ML service is unavailable: " + ex.getMessage());
        }
    }
}