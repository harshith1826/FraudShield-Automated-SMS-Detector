package com.fraudshield.controller;

import com.fraudshield.dto.ApiResponse;
import com.fraudshield.dto.BlacklistEntryDto;
import com.fraudshield.dto.DeltaSyncResponse;
import com.fraudshield.dto.IngestRequest;
import com.fraudshield.dto.IngestResponse;
import com.fraudshield.dto.UserFeedbackRequest;
import com.fraudshield.dto.VerdictResponse;
import com.fraudshield.dto.WhitelistEntryDto;
import com.fraudshield.model.SmsAnalysis;
import com.fraudshield.service.SmsDetectionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class AndroidController {

    private final SmsDetectionService smsDetectionService;

    public AndroidController(SmsDetectionService smsDetectionService) {
        this.smsDetectionService = smsDetectionService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<IngestResponse> ingest(@Valid @RequestBody IngestRequest request) {
        log.info("Android ingest from sender: {}, device: {}",
                request.getSenderId(), request.getDeviceId());

        long timestamp = request.getTimestamp() > 0
                ? request.getTimestamp()
                : System.currentTimeMillis();

        SmsAnalysis analysis = smsDetectionService.analyzeForAndroid(
                request.getSmsBody(),
                request.getSenderId(),
                request.getExtractedUrl(),
                request.getDeviceId(),
                timestamp
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                IngestResponse.builder()
                        .status("processed")
                        .jobId(analysis.getId())
                        .build()
        );
    }

    @GetMapping("/getmlfeedback/{job_id}")
    public ResponseEntity<VerdictResponse> getMlFeedback(
            @PathVariable("job_id") String jobId) {
        log.info("Android ML feedback request for job_id: {}", jobId);

        SmsAnalysis analysis = smsDetectionService.getById(jobId);

        return ResponseEntity.ok(VerdictResponse.builder()
                .url(analysis.getPrimaryUrl() != null ? analysis.getPrimaryUrl() : "")
                .verdict(analysis.getPrediction() != null ? analysis.getPrediction() : "UNKNOWN")
                .confidence(analysis.getConfidence())
                .category(analysis.getCategory())
                .warningTextEnglish(analysis.getWarningTextEnglish())
                .warningTextHindi(analysis.getWarningTextHindi())
                .warningTextTamil(analysis.getWarningTextTamil())
                .warningTextTelugu(analysis.getWarningTextTelugu())
                .warningTextKannada(analysis.getWarningTextKannada())
                .campaignDetected(analysis.isCampaignDetected())
                .specificThreat(analysis.getSpecificThreat())
                .action(analysis.getAction())
                .build());
    }

    @PostMapping("/userfeedback")
    public ResponseEntity<ApiResponse<Void>> userFeedback(
            @Valid @RequestBody UserFeedbackRequest request) {
        log.info("User feedback: url={}, signal={}, verdict={}",
                request.getUrl(), request.getUserSignal(), request.getCurrentSystemVerdict());
        return ResponseEntity.ok(ApiResponse.success("Feedback received successfully", null));
    }

    @GetMapping("/delta-sync")
    public ResponseEntity<DeltaSyncResponse> deltaSync() {
        log.info("Delta sync requested");
        return ResponseEntity.ok(DeltaSyncResponse.builder()
                .newWhitelistEntries(List.of(
                        WhitelistEntryDto.builder().domain("sbi.co.in")
                                .organisation("State Bank of India").trustScore(1.0f).build(),
                        WhitelistEntryDto.builder().domain("hdfcbank.com")
                                .organisation("HDFC Bank").trustScore(1.0f).build(),
                        WhitelistEntryDto.builder().domain("icicibank.com")
                                .organisation("ICICI Bank").trustScore(1.0f).build(),
                        WhitelistEntryDto.builder().domain("phonepe.com")
                                .organisation("PhonePe").trustScore(1.0f).build(),
                        WhitelistEntryDto.builder().domain("paytm.com")
                                .organisation("Paytm").trustScore(1.0f).build()
                ))
                .newBlacklistEntries(List.of(
                        BlacklistEntryDto.builder().domain("bit.ly")
                                .confidence(0.85f).category("url_shortener").build(),
                        BlacklistEntryDto.builder().domain("tinyurl.com")
                                .confidence(0.85f).category("url_shortener").build()
                ))
                .lastSyncTimestamp(System.currentTimeMillis())
                .build());
    }
}