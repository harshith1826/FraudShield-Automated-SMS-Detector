package com.fraudshield.controller;

import com.fraudshield.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        log.info("Health check requested");
        Map<String, String> healthData = Map.of(
                "status", "UP",
                "service", "FraudShield Backend",
                "version", "1.0.0"
        );
        return ResponseEntity.ok(ApiResponse.success("Service is running", healthData));
    }
}