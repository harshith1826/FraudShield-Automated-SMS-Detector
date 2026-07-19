package com.fraudshield.controller;

import com.fraudshield.dto.ApiResponse;
import com.fraudshield.dto.SmsRequest;
import com.fraudshield.dto.SmsResponse;
import com.fraudshield.model.SmsAnalysis;
import com.fraudshield.service.SmsDetectionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {

    private final SmsDetectionService smsDetectionService;

    public SmsController(SmsDetectionService smsDetectionService) {
        this.smsDetectionService = smsDetectionService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<SmsResponse>> analyze(
            @Valid @RequestBody SmsRequest smsRequest) {
        log.info("Received SMS analysis request from sender: {}", smsRequest.getSender());
        SmsResponse smsResponse = smsDetectionService.analyze(smsRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("SMS analyzed successfully", smsResponse));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<SmsAnalysis>>> getHistory() {
        log.info("Received request to fetch SMS analysis history");
        List<SmsAnalysis> history = smsDetectionService.getHistory();
        return ResponseEntity.ok(ApiResponse.success("History retrieved successfully", history));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SmsAnalysis>> getById(@PathVariable String id) {
        log.info("Received request to fetch SMS analysis with id: {}", id);
        SmsAnalysis smsAnalysis = smsDetectionService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Record retrieved successfully", smsAnalysis));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable String id) {
        log.info("Received request to delete SMS analysis with id: {}", id);
        smsDetectionService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Record deleted successfully", null));
    }
}