package com.fraudshield.service;

import com.fraudshield.dto.BlacklistEntryView;
import com.fraudshield.dto.DashboardStatsResponse;
import com.fraudshield.dto.PagedHistoryResponse;
import com.fraudshield.dto.WhitelistEntryView;
import com.fraudshield.model.SmsAnalysis;
import com.fraudshield.repository.SmsAnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class DashboardService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Set<String> WHITELIST_DOMAINS = Set.of(
            "sbi.co.in", "hdfcbank.com", "icicibank.com", "phonepe.com", "paytm.com",
            "axisbank.com", "kotak.com", "uidai.gov.in", "irctc.co.in", "google.com",
            "amazon.in", "flipkart.com"
    );

    private static final Set<String> BLACKLIST_DOMAINS = Set.of(
            "bit.ly", "tinyurl.com"
    );

    private final SmsAnalysisRepository smsAnalysisRepository;
    private final RestTemplate restTemplate;
    private final String fastApiBaseUrl;

    public DashboardService(SmsAnalysisRepository smsAnalysisRepository,
                             RestTemplate restTemplate,
                             @Value("${fastapi.base-url}") String fastApiBaseUrl) {
        this.smsAnalysisRepository = smsAnalysisRepository;
        this.restTemplate = restTemplate;
        this.fastApiBaseUrl = fastApiBaseUrl;
    }

    public DashboardStatsResponse getStats() {
        long startTime = System.currentTimeMillis();
        List<SmsAnalysis> allRecords = smsAnalysisRepository.findAll();
        log.info("Computing dashboard stats over {} records", allRecords.size());

        long totalSms = allRecords.size();
        long fraudSms = 0;
        long safeSms = 0;
        long suspiciousSms = 0;
        long blockedByBlacklist = 0;
        long allowedByWhitelist = 0;
        long totalAiPredictions = 0;
        long todaysCount = 0;
        double confidenceSum = 0;
        long confidenceCount = 0;

        String today = LocalDate.now().format(DATE_FORMATTER);

        Map<String, Long> verdictDistribution = new HashMap<>();
        Map<String, Long> categoryDistribution = new HashMap<>();
        Map<String, Long> confidenceBuckets = new HashMap<>();
        Map<String, Long> dailyTotal = new HashMap<>();
        Map<String, long[]> dailyBuckets = new HashMap<>();
        Map<String, Long> fraudSenderCounts = new HashMap<>();
        Map<String, Long> fraudUrlCounts = new HashMap<>();

        for (SmsAnalysis record : allRecords) {
            String verdict = normalize(record.getPrediction());
            verdictDistribution.merge(verdict, 1L, Long::sum);

            boolean isFraud = "FRAUD".equalsIgnoreCase(verdict);
            if (isFraud) {
                fraudSms++;
                if (record.getSender() != null) {
                    fraudSenderCounts.merge(record.getSender(), 1L, Long::sum);
                }
                if (record.getPrimaryUrl() != null && !record.getPrimaryUrl().isBlank()) {
                    fraudUrlCounts.merge(record.getPrimaryUrl(), 1L, Long::sum);
                }
            } else if ("SAFE".equalsIgnoreCase(verdict)) {
                safeSms++;
            } else if ("SUSPICIOUS".equalsIgnoreCase(verdict)) {
                suspiciousSms++;
            }

            String category = normalize(record.getCategory());
            categoryDistribution.merge(category, 1L, Long::sum);

            if (record.getPrediction() != null) {
                totalAiPredictions++;
                confidenceSum += record.getConfidence();
                confidenceCount++;
                confidenceBuckets.merge(confidenceBucketLabel(record.getConfidence()), 1L, Long::sum);
            }

            if (isBlacklisted(record)) {
                blockedByBlacklist++;
            }
            if (isWhitelisted(record)) {
                allowedByWhitelist++;
            }

            if (record.getAnalyzedAt() != null) {
                String dateKey = record.getAnalyzedAt().toLocalDate().format(DATE_FORMATTER);
                if (dateKey.equals(today)) {
                    todaysCount++;
                }
                long[] bucket = dailyBuckets.computeIfAbsent(dateKey, k -> new long[3]);
                bucket[0] += 1;
                if (isFraud) {
                    bucket[1] += 1;
                } else if ("SAFE".equalsIgnoreCase(verdict)) {
                    bucket[2] += 1;
                }
            }
        }

        double averageConfidence = confidenceCount > 0 ? confidenceSum / confidenceCount : 0.0;

        List<DashboardStatsResponse.DailyCount> dailyCounts = buildDailyCounts(dailyBuckets);
        List<DashboardStatsResponse.TopEntry> topFraudSenders = buildTopEntries(fraudSenderCounts, 5);
        List<DashboardStatsResponse.TopEntry> topFraudUrls = buildTopEntries(fraudUrlCounts, 5);

        DashboardStatsResponse.SystemStatus systemStatus = checkSystemStatus(startTime);

        return DashboardStatsResponse.builder()
                .totalSmsAnalysed(totalSms)
                .fraudSms(fraudSms)
                .safeSms(safeSms)
                .suspiciousSms(suspiciousSms)
                .blockedByBlacklist(blockedByBlacklist)
                .allowedByWhitelist(allowedByWhitelist)
                .totalAiPredictions(totalAiPredictions)
                .averageConfidence(Math.round(averageConfidence * 10.0) / 10.0)
                .todaysAnalysisCount(todaysCount)
                .verdictDistribution(verdictDistribution)
                .categoryDistribution(categoryDistribution)
                .confidenceDistribution(confidenceBuckets)
                .dailyAnalysisCounts(dailyCounts)
                .topFraudSenders(topFraudSenders)
                .topFraudUrls(topFraudUrls)
                .systemStatus(systemStatus)
                .build();
    }

    public PagedHistoryResponse getHistory(String sender, String verdict, String url, String date,
                                            String search, int page, int size, String sortBy, String sortDir) {
        List<SmsAnalysis> allRecords = smsAnalysisRepository.findAllByOrderByAnalyzedAtDesc();

        List<SmsAnalysis> filtered = allRecords.stream()
                .filter(r -> sender == null || sender.isBlank()
                        || (r.getSender() != null && r.getSender().toLowerCase().contains(sender.toLowerCase())))
                .filter(r -> verdict == null || verdict.isBlank()
                        || (r.getPrediction() != null && r.getPrediction().equalsIgnoreCase(verdict)))
                .filter(r -> url == null || url.isBlank()
                        || (r.getPrimaryUrl() != null && r.getPrimaryUrl().toLowerCase().contains(url.toLowerCase())))
                .filter(r -> date == null || date.isBlank()
                        || (r.getAnalyzedAt() != null
                        && r.getAnalyzedAt().toLocalDate().format(DATE_FORMATTER).equals(date)))
                .filter(r -> search == null || search.isBlank() || matchesSearch(r, search))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

        applySort(filtered, sortBy, sortDir);

        int totalRecords = filtered.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        int fromIndex = Math.min(page * size, totalRecords);
        int toIndex = Math.min(fromIndex + size, totalRecords);
        List<SmsAnalysis> pageContent = filtered.subList(fromIndex, toIndex);

        return PagedHistoryResponse.builder()
                .records(pageContent)
                .totalRecords(totalRecords)
                .totalPages(Math.max(totalPages, 1))
                .currentPage(page)
                .pageSize(size)
                .build();
    }

    public List<WhitelistEntryView> getWhitelist() {
        List<SmsAnalysis> allRecords = smsAnalysisRepository.findAll();
        List<WhitelistEntryView> result = new ArrayList<>();

        for (SmsAnalysis record : allRecords) {
            if (isWhitelisted(record)) {
                result.add(WhitelistEntryView.builder()
                        .sender(record.getSender())
                        .url(record.getPrimaryUrl())
                        .type("Verified Legitimate")
                        .createdDate(record.getAnalyzedAt() != null
                                ? record.getAnalyzedAt().toLocalDate().format(DATE_FORMATTER) : "-")
                        .build());
            }
        }
        return result;
    }

    public List<BlacklistEntryView> getBlacklist() {
        List<SmsAnalysis> allRecords = smsAnalysisRepository.findAll();
        List<BlacklistEntryView> result = new ArrayList<>();

        for (SmsAnalysis record : allRecords) {
            if (isBlacklisted(record)) {
                result.add(BlacklistEntryView.builder()
                        .sender(record.getSender())
                        .url(record.getPrimaryUrl())
                        .type(normalize(record.getCategory()))
                        .createdDate(record.getAnalyzedAt() != null
                                ? record.getAnalyzedAt().toLocalDate().format(DATE_FORMATTER) : "-")
                        .build());
            }
        }
        return result;
    }

    private boolean matchesSearch(SmsAnalysis r, String search) {
        String s = search.toLowerCase();
        return (r.getSender() != null && r.getSender().toLowerCase().contains(s))
                || (r.getMessage() != null && r.getMessage().toLowerCase().contains(s))
                || (r.getPrimaryUrl() != null && r.getPrimaryUrl().toLowerCase().contains(s))
                || (r.getPrediction() != null && r.getPrediction().toLowerCase().contains(s));
    }

    private void applySort(List<SmsAnalysis> list, String sortBy, String sortDir) {
        Comparator<SmsAnalysis> comparator;
        String field = (sortBy == null || sortBy.isBlank()) ? "analyzedAt" : sortBy;

        switch (field) {
            case "confidence" -> comparator = Comparator.comparingDouble(SmsAnalysis::getConfidence);
            case "sender" -> comparator = Comparator.comparing(
                    r -> r.getSender() == null ? "" : r.getSender(), String.CASE_INSENSITIVE_ORDER);
            case "verdict" -> comparator = Comparator.comparing(
                    r -> r.getPrediction() == null ? "" : r.getPrediction(), String.CASE_INSENSITIVE_ORDER);
            default -> comparator = Comparator.comparing(
                    r -> r.getAnalyzedAt() == null ? java.time.LocalDateTime.MIN : r.getAnalyzedAt());
        }

        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }
        list.sort(comparator);
    }

    private DashboardStatsResponse.SystemStatus checkSystemStatus(long startTime) {
        boolean mongoUp = true;
        boolean aiUp;
        try {
            smsAnalysisRepository.count();
        } catch (Exception ex) {
            mongoUp = false;
        }

        try {
            restTemplate.getForEntity(fastApiBaseUrl + "/health", String.class);
            aiUp = true;
        } catch (Exception ex) {
            aiUp = false;
        }

        long responseTime = System.currentTimeMillis() - startTime;

        return DashboardStatsResponse.SystemStatus.builder()
                .mongoUp(mongoUp)
                .aiServiceReachable(aiUp)
                .responseTimeMs(responseTime)
                .build();
    }

    private boolean isWhitelisted(SmsAnalysis record) {
        if (record.getPrimaryUrl() == null) {
            return false;
        }
        String url = record.getPrimaryUrl().toLowerCase();
        return WHITELIST_DOMAINS.stream().anyMatch(url::contains);
    }

    private boolean isBlacklisted(SmsAnalysis record) {
        if (record.getPrimaryUrl() == null) {
            return false;
        }
        String url = record.getPrimaryUrl().toLowerCase();
        boolean knownBadDomain = BLACKLIST_DOMAINS.stream().anyMatch(url::contains);
        boolean blockedByAction = "BLOCK".equalsIgnoreCase(record.getAction());
        return knownBadDomain || blockedByAction;
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? "UNKNOWN" : value.toUpperCase();
    }

    private String confidenceBucketLabel(double confidence) {
        if (confidence >= 90) return "90-100%";
        if (confidence >= 70) return "70-89%";
        if (confidence >= 50) return "50-69%";
        if (confidence >= 30) return "30-49%";
        return "0-29%";
    }

    private List<DashboardStatsResponse.DailyCount> buildDailyCounts(Map<String, long[]> dailyBuckets) {
        List<DashboardStatsResponse.DailyCount> result = new ArrayList<>();
        for (Map.Entry<String, long[]> entry : dailyBuckets.entrySet()) {
            long[] bucket = entry.getValue();
            result.add(DashboardStatsResponse.DailyCount.builder()
                    .date(entry.getKey())
                    .count(bucket[0])
                    .fraudCount(bucket[1])
                    .safeCount(bucket[2])
                    .build());
        }
        result.sort(Comparator.comparing(DashboardStatsResponse.DailyCount::getDate));
        return result;
    }

    private List<DashboardStatsResponse.TopEntry> buildTopEntries(Map<String, Long> counts, int limit) {
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(e -> DashboardStatsResponse.TopEntry.builder()
                        .label(e.getKey())
                        .count(e.getValue())
                        .build())
                .toList();
    }
}