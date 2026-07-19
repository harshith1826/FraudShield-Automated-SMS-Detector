package com.fraudshield.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fraudshield.util.UrlExtractor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UrlScannerService {

    private final UrlExtractor urlExtractor;

    public UrlScannerService(UrlExtractor urlExtractor) {
        this.urlExtractor = urlExtractor;
    }

    public List<String> extractAndScan(String message) {
        List<String> urls = urlExtractor.extract(message);
        log.info("Extracted {} URL(s) from message", urls.size());

        if (!urls.isEmpty()) {
            log.info("URLs found: {}", urls);
        }

        return urls;
    }

    public boolean messageContainsUrl(String message) {
        return urlExtractor.containsUrl(message);
    }
}