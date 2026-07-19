package com.fraudshield.app;

/**
 * Pulls a URL out of raw SMS text. Handles:
 * - standard https://...
 * - no protocol (sbi-kyc.xyz/login)
 * - shortened (bit.ly/3xKj9p)
 * - embedded mid-sentence ("update at sbi-kyc.xyz today")
 * - IP address URLs (http://192.168.1.1/bank/login)
 * - with path/query params
 *
 * Does NOT resolve shortened URLs or punycode — that's the backend's job
 * (Navya's redirect-chain resolver). We just need the raw extracted string.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/fraudshield/app/UrlExtractor;", "", "()V", "URL_REGEX", "Lkotlin/text/Regex;", "extract", "", "smsBody", "extractDomain", "url", "app_debug"})
public final class UrlExtractor {
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex URL_REGEX = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.fraudshield.app.UrlExtractor INSTANCE = null;
    
    private UrlExtractor() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String extract(@org.jetbrains.annotations.NotNull()
    java.lang.String smsBody) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String extractDomain(@org.jetbrains.annotations.NotNull()
    java.lang.String url) {
        return null;
    }
}