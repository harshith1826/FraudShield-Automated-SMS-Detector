package com.fraudshield.service;

import com.fraudshield.dto.MlResponse;
import com.fraudshield.dto.SmsRequest;
import com.fraudshield.dto.SmsResponse;
import com.fraudshield.exception.ResourceNotFoundException;
import com.fraudshield.model.SmsAnalysis;
import com.fraudshield.repository.SmsAnalysisRepository;
import com.fraudshield.util.RiskCalculator;
import com.fraudshield.util.UrlExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SmsDetectionService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SmsAnalysisRepository smsAnalysisRepository;
    private final AiPredictionService aiPredictionService;
    private final UrlScannerService urlScannerService;
    private final RiskCalculator riskCalculator;
    private final UrlExtractor urlExtractor;

    public SmsDetectionService(SmsAnalysisRepository smsAnalysisRepository,
                                AiPredictionService aiPredictionService,
                                UrlScannerService urlScannerService,
                                RiskCalculator riskCalculator,
                                UrlExtractor urlExtractor) {
        this.smsAnalysisRepository = smsAnalysisRepository;
        this.aiPredictionService = aiPredictionService;
        this.urlScannerService = urlScannerService;
        this.riskCalculator = riskCalculator;
        this.urlExtractor = urlExtractor;
    }

    public SmsResponse analyze(SmsRequest request) {
        log.info("Starting SMS analysis for sender: {}", request.getSender());

        List<String> extractedUrls = urlScannerService.extractAndScan(request.getMessage());
        boolean containsUrl = !extractedUrls.isEmpty();

        int riskScore = riskCalculator.calculate(request.getMessage(), extractedUrls);
        String threatLevel = riskCalculator.determineThreatLevel(riskScore);
        log.info("Rule-based risk score: {}, Threat level: {}", riskScore, threatLevel);

        MlResponse mlResponse = aiPredictionService.predict(
                request.getMessage(), request.getSender(), extractedUrls);

        int combinedRiskScore = combineRiskScore(riskScore, mlResponse);
        String finalThreatLevel = riskCalculator.determineThreatLevel(combinedRiskScore);

        LocalDateTime analyzedAt = LocalDateTime.now();
        String primaryUrl = extractedUrls.isEmpty() ? null : extractedUrls.get(0);
        String reasonSummary = buildReasonSummary(mlResponse);

        SmsAnalysis smsAnalysis = buildSmsAnalysis(
                request.getMessage(), request.getSender(), null,
                extractedUrls, containsUrl, combinedRiskScore,
                finalThreatLevel, reasonSummary, analyzedAt, primaryUrl, mlResponse
        );

        SmsAnalysis savedAnalysis = smsAnalysisRepository.save(smsAnalysis);
        log.info("SMS analysis saved with id: {}", savedAnalysis.getId());

        return SmsResponse.builder()
                .prediction(mlResponse.getVerdict())
                .confidence(mlResponse.getConfidence() * 100)
                .riskScore(combinedRiskScore)
                .threatLevel(finalThreatLevel)
                .reason(reasonSummary)
                .analyzedAt(analyzedAt.format(FORMATTER))
                .build();
    }

    public SmsAnalysis analyzeForAndroid(String smsBody, String senderId,
                                          String extractedUrl, String deviceId, long timestamp) {
        log.info("Starting Android SMS analysis for sender: {}", senderId);

        List<String> extractedUrls = new ArrayList<>(urlExtractor.extract(smsBody));
        if (extractedUrl != null && !extractedUrl.isBlank() && !extractedUrls.contains(extractedUrl)) {
            extractedUrls.add(0, extractedUrl);
        }
        boolean containsUrl = !extractedUrls.isEmpty();

        int riskScore = riskCalculator.calculate(smsBody, extractedUrls);

        MlResponse mlResponse = aiPredictionService.predict(smsBody, senderId, extractedUrls);

        int combinedRiskScore = combineRiskScore(riskScore, mlResponse);
        String finalThreatLevel = riskCalculator.determineThreatLevel(combinedRiskScore);

        LocalDateTime analyzedAt = LocalDateTime.now();
        String primaryUrl = extractedUrls.isEmpty() ? null : extractedUrls.get(0);
        String reasonSummary = buildReasonSummary(mlResponse);

        SmsAnalysis smsAnalysis = buildSmsAnalysis(
                smsBody, senderId, deviceId,
                extractedUrls, containsUrl, combinedRiskScore,
                finalThreatLevel, reasonSummary, analyzedAt, primaryUrl, mlResponse
        );

        SmsAnalysis saved = smsAnalysisRepository.save(smsAnalysis);
        log.info("Android SMS analysis saved with id: {}", saved.getId());
        return saved;
    }

    public List<SmsAnalysis> getHistory() {
        log.info("Fetching all SMS analysis records");
        return smsAnalysisRepository.findAllByOrderByAnalyzedAtDesc();
    }

    public SmsAnalysis getById(String id) {
        log.info("Fetching SMS analysis record with id: {}", id);
        return smsAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SMS analysis record not found with id: " + id));
    }

    public void deleteById(String id) {
        log.info("Deleting SMS analysis record with id: {}", id);
        if (!smsAnalysisRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "SMS analysis record not found with id: " + id);
        }
        smsAnalysisRepository.deleteById(id);
    }

    private SmsAnalysis buildSmsAnalysis(String message, String sender, String deviceId,
                                          List<String> extractedUrls, boolean containsUrl,
                                          int combinedRiskScore, String finalThreatLevel,
                                          String reasonSummary, LocalDateTime analyzedAt,
                                          String primaryUrl, MlResponse mlResponse) {
        return SmsAnalysis.builder()
                .message(message)
                .sender(sender)
                .deviceId(deviceId)
                .prediction(mlResponse.getVerdict())
                .confidence(mlResponse.getConfidence() * 100)
                .riskScore(combinedRiskScore)
                .threatLevel(finalThreatLevel)
                .reason(reasonSummary)
                .containsUrl(containsUrl)
                .extractedUrls(extractedUrls)
                .analyzedAt(analyzedAt)
                .fraudScore(mlResponse.getFraudScore())
                .category(mlResponse.getCategory())
                .action(mlResponse.getAction())
                .reasons(mlResponse.getReasons())
                .signals(mlResponse.getSignals())
                .primaryUrl(primaryUrl)
                .warningTextEnglish(mlResponse.getWarningTextEnglish())
                .warningTextHindi(mlResponse.getWarningTextHindi())
                .warningTextTamil(mlResponse.getWarningTextTamil())
                .warningTextTelugu(mlResponse.getWarningTextTelugu())
                .warningTextKannada(mlResponse.getWarningTextKannada())
                .campaignDetected(mlResponse.isCampaignDetected())
                .specificThreat(mlResponse.getSpecificThreat())
                .build();
    }

    private String buildReasonSummary(MlResponse mlResponse) {
        if (mlResponse.getReasons() != null && !mlResponse.getReasons().isEmpty()) {
            return String.join("; ", mlResponse.getReasons());
        }
        return "No reason provided";
    }

    private int combineRiskScore(int ruleBasedScore, MlResponse mlResponse) {
        double aiWeightedScore = "FRAUD".equalsIgnoreCase(mlResponse.getVerdict())
                ? mlResponse.getConfidence() * 100
                : (1.0 - mlResponse.getConfidence()) * 100;

        int combined = (int) Math.round((ruleBasedScore * 0.4) + (aiWeightedScore * 0.6));
        return Math.min(combined, 100);
    }
}