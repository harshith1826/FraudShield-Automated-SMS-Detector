package com.fraudshield.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DashboardStatsResponse {

    private long totalSmsAnalysed;
    private long fraudSms;
    private long safeSms;
    private long suspiciousSms;
    private long blockedByBlacklist;
    private long allowedByWhitelist;
    private long totalAiPredictions;
    private double averageConfidence;
    private long todaysAnalysisCount;

    private Map<String, Long> verdictDistribution;
    private Map<String, Long> categoryDistribution;
    private Map<String, Long> confidenceDistribution;
    private List<DailyCount> dailyAnalysisCounts;
    private List<TopEntry> topFraudSenders;
    private List<TopEntry> topFraudUrls;
    private SystemStatus systemStatus;

    @Getter
    @Builder
    public static class DailyCount {
        private String date;
        private long count;
        private long fraudCount;
        private long safeCount;
    }

    @Getter
    @Builder
    public static class TopEntry {
        private String label;
        private long count;
    }

    @Getter
    @Builder
    public static class SystemStatus {
        private boolean mongoUp;
        private boolean aiServiceReachable;
        private long responseTimeMs;
    }
}