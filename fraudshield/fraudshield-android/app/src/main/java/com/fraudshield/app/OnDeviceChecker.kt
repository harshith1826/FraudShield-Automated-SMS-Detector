package com.fraudshield.app

import com.fraudshield.app.db.AppDatabase

data class CheckResult(
    val verdict: String,     // SAFE / FRAUD / SUSPICIOUS / PENDING
    val riskScore: Float,    // 0-100, higher = more suspicious
    val flagsRaised: List<String> = emptyList()
)

object OnDeviceChecker {

    suspend fun check(db: AppDatabase, senderId: String, url: String): CheckResult {
        val domain = UrlExtractor.extractDomain(url)

        // --- Check A: local cache lookup (instant) ---
        db.urlCacheDao().findByUrl(url)?.let { cached ->
            when (cached.status) {
                "whitelist" -> return CheckResult("SAFE", 0f)
                "blacklist" -> return CheckResult("FRAUD", 100f, listOf("BLACKLIST_HIT"))
                else -> { /* "pending" or unknown status — fall through to Check B/C below */ }
            }
        }

        val flags = mutableListOf<String>()
        var riskScore = 0f

        // --- Check B: sender ID cross-check ---
        val sender = db.senderAllowlistDao().find(senderId)
        if (sender != null) {
            val knownDomains = sender.associatedDomains.split(",").map { it.trim() }
            val matchesKnownDomain = knownDomains.any { domain.contains(it, ignoreCase = true) }
            if (!matchesKnownDomain) {
                flags.add("SENDER_DOMAIN_MISMATCH")
                riskScore += 30f
            }
        }

        // --- Check C: pattern matching (TLD + keywords) ---
        val patterns = db.patternDao().getAll()
        patterns.forEach { p ->
            when (p.patternType) {
                "tld" -> if (domain.endsWith(p.pattern, ignoreCase = true)) {
                    flags.add("HIGH_RISK_TLD:${p.pattern}")
                    riskScore += p.riskScore * 20f
                }
                "keyword" -> if (domain.contains(p.pattern, ignoreCase = true)) {
                    flags.add("FRAUD_KEYWORD:${p.pattern}")
                    riskScore += p.riskScore * 20f
                }
            }
        }

        // Brand impersonation heuristic: domain has a hyphen + a bank-ish word but
        // isn't in the sender's known domains
        if (domain.contains("-") && sender != null && flags.contains("SENDER_DOMAIN_MISMATCH")) {
            flags.add("BRAND_IMPERSONATION_PATTERN")
            riskScore += 15f
        }

        riskScore = riskScore.coerceAtMost(100f)

        val verdict = when {
            flags.size >= 2 || riskScore >= 50f -> "SUSPICIOUS"
            flags.isNotEmpty() -> "SUSPICIOUS"
            else -> "PENDING" // not enough signal on-device, wait for server
        }

        return CheckResult(verdict, riskScore, flags)
    }
}
