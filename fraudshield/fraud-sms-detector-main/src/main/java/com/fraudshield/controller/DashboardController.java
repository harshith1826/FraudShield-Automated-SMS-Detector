package com.fraudshield.controller;

import com.fraudshield.dto.ApiResponse;
import com.fraudshield.dto.BlacklistEntryView;
import com.fraudshield.dto.DashboardStatsResponse;
import com.fraudshield.dto.PagedHistoryResponse;
import com.fraudshield.dto.WhitelistEntryView;
import com.fraudshield.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getStats() {
        log.info("Dashboard stats requested");
        DashboardStatsResponse stats = dashboardService.getStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved successfully", stats));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PagedHistoryResponse>> getHistory(
            @RequestParam(required = false) String sender,
            @RequestParam(required = false) String verdict,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "analyzedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("Dashboard history requested - sender:{}, verdict:{}, url:{}, date:{}, search:{}, page:{}",
                sender, verdict, url, date, search, page);
        PagedHistoryResponse history = dashboardService.getHistory(
                sender, verdict, url, date, search, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success("History retrieved successfully", history));
    }

    @GetMapping("/whitelist")
    public ResponseEntity<ApiResponse<List<WhitelistEntryView>>> getWhitelist() {
        log.info("Dashboard whitelist requested");
        List<WhitelistEntryView> whitelist = dashboardService.getWhitelist();
        return ResponseEntity.ok(ApiResponse.success("Whitelist retrieved successfully", whitelist));
    }

    @GetMapping("/blacklist")
    public ResponseEntity<ApiResponse<List<BlacklistEntryView>>> getBlacklist() {
        log.info("Dashboard blacklist requested");
        List<BlacklistEntryView> blacklist = dashboardService.getBlacklist();
        return ResponseEntity.ok(ApiResponse.success("Blacklist retrieved successfully", blacklist));
    }
}