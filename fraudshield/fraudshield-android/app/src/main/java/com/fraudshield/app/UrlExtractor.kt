package com.fraudshield.app

import android.net.Uri

/**
 * Pulls a URL out of raw SMS text. Handles:
 *  - standard https://...
 *  - no protocol (sbi-kyc.xyz/login)
 *  - shortened (bit.ly/3xKj9p)
 *  - embedded mid-sentence ("update at sbi-kyc.xyz today")
 *  - IP address URLs (http://192.168.1.1/bank/login)
 *  - with path/query params
 *
 * Does NOT resolve shortened URLs or punycode — that's the backend's job
 * (Navya's redirect-chain resolver). We just need the raw extracted string.
 */
object UrlExtractor {

    private val URL_REGEX = Regex(
        pattern = """((https?://)?([\w-]+\.)+[a-zA-Z]{2,}(/[^\s]*)?|https?://\d{1,3}(\.\d{1,3}){3}(/[^\s]*)?)""",
        option = RegexOption.IGNORE_CASE
    )

    fun extract(smsBody: String): String? {
        val match = URL_REGEX.find(smsBody) ?: return null
        var url = match.value.trim().trimEnd('.', ',', ')', '!', '?')
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://$url"
        }
        return url
    }

    fun extractDomain(url: String): String {
        return try {
            Uri.parse(url).host ?: url
        } catch (e: Exception) {
            url
        }
    }
}
