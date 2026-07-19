package com.fraudshield.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class UrlExtractor {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "(?i)\\b(?:https?://|www\\.|bit\\.ly/|tinyurl\\.com/)[^\\s,;\"'<>()\\[\\]{}]+",
            Pattern.CASE_INSENSITIVE
    );

    private static final List<String> SHORTENER_PREFIXES = List.of(
            "bit.ly", "tinyurl.com"
    );

    public List<String> extract(String message) {
        List<String> urls = new ArrayList<>();

        if (message == null || message.isBlank()) {
            return urls;
        }

        Matcher matcher = URL_PATTERN.matcher(message);
        while (matcher.find()) {
            String url = matcher.group().trim();
            if (!urls.contains(url)) {
                urls.add(url);
            }
        }

        return urls;
    }

    public boolean containsUrl(String message) {
        if (message == null || message.isBlank()) {
            return false;
        }
        return URL_PATTERN.matcher(message).find();
    }

    public boolean containsShortenedUrl(List<String> urls) {
        return urls.stream().anyMatch(url ->
                SHORTENER_PREFIXES.stream().anyMatch(prefix ->
                        url.toLowerCase().contains(prefix)
                )
        );
    }
}