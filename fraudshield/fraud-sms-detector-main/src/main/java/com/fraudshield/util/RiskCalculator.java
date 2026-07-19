package com.fraudshield.util;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RiskCalculator {

    private static final List<String> LOTTERY_KEYWORDS = List.of(
            "lottery", "congratulations", "winner", "won", "prize",
            "claim", "reward", "lucky", "jackpot", "selected"
    );

    private static final List<String> FINANCIAL_KEYWORDS = List.of(
            "bank", "account", "verify", "kyc", "otp", "pin",
            "password", "upi", "payment", "transaction", "refund", "credit"
    );

    private static final List<String> URGENCY_KEYWORDS = List.of(
            "urgent", "immediately", "expires", "limited offer",
            "act now", "last chance", "hurry", "deadline", "asap", "today only"
    );

    private static final List<String> SUSPICIOUS_URL_PATTERNS = List.of(
            "bit.ly", "tinyurl", "click", "free", "win", "prize", "claim", "lucky"
    );

    private static final int LOTTERY_KEYWORD_SCORE = 15;
    private static final int FINANCIAL_KEYWORD_SCORE = 12;
    private static final int URGENCY_KEYWORD_SCORE = 10;
    private static final int URL_PRESENT_SCORE = 10;
    private static final int SHORTENED_URL_SCORE = 15;
    private static final int SUSPICIOUS_URL_PATTERN_SCORE = 8;
    private static final int MAX_SCORE = 100;

    private final UrlExtractor urlExtractor;

    public RiskCalculator(UrlExtractor urlExtractor) {
        this.urlExtractor = urlExtractor;
    }

    public int calculate(String message, List<String> extractedUrls) {
        if (message == null || message.isBlank()) {
            return 0;
        }

        String normalizedMessage = message.toLowerCase();
        int score = 0;

        score += scoreKeywords(normalizedMessage, LOTTERY_KEYWORDS, LOTTERY_KEYWORD_SCORE);
        score += scoreKeywords(normalizedMessage, FINANCIAL_KEYWORDS, FINANCIAL_KEYWORD_SCORE);
        score += scoreKeywords(normalizedMessage, URGENCY_KEYWORDS, URGENCY_KEYWORD_SCORE);

        if (!extractedUrls.isEmpty()) {
            score += URL_PRESENT_SCORE;

            if (urlExtractor.containsShortenedUrl(extractedUrls)) {
                score += SHORTENED_URL_SCORE;
            }

            for (String url : extractedUrls) {
                String lowerUrl = url.toLowerCase();
                boolean hasSuspiciousPattern = SUSPICIOUS_URL_PATTERNS.stream()
                        .anyMatch(lowerUrl::contains);
                if (hasSuspiciousPattern) {
                    score += SUSPICIOUS_URL_PATTERN_SCORE;
                    break;
                }
            }
        }

        return Math.min(score, MAX_SCORE);
    }

    public String determineThreatLevel(int riskScore) {
        if (riskScore >= 70) {
            return "HIGH";
        } else if (riskScore >= 40) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    private int scoreKeywords(String message, List<String> keywords, int scorePerHit) {
        int hits = 0;
        for (String keyword : keywords) {
            if (message.contains(keyword.toLowerCase())) {
                hits++;
            }
        }
        return hits * scorePerHit;
    }
}