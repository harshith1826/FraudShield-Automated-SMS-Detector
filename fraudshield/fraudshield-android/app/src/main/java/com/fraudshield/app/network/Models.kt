package com.fraudshield.app.network

// --- POST /api/ingest ---
data class IngestRequest(
    val sender_id: String,
    val sms_body: String,
    val extracted_url: String,
    val timestamp: Long,
    val device_id: String
)

data class IngestResponse(
    val status: String,
    val job_id: String
)

// --- GET /api/getmlfeedback/{job_id} ---
data class VerdictResponse(
    val url: String,
    val verdict: String,              // "FRAUD" | "SAFE" | "SUSPICIOUS"
    val confidence: Float,
    val category: String?,
    val warning_text_english: String?,
    val warning_text_hindi: String?,
    val warning_text_tamil: String?,
    val warning_text_telugu: String?,
    val warning_text_kannada: String?,
    val campaign_detected: Boolean?,
    val specific_threat: String?,
    val action: String?               // "BLOCK" | "ALLOW" | "WARN"
)

// --- POST /api/userfeedback ---
data class UserFeedbackRequest(
    val url: String,
    val user_signal: Int,             // 1 = fraud confirmed, 0 = safe
    val current_system_verdict: String,
    val user_age_group: String? = null,
    val device_id: String,
    val timestamp: Long
)

// --- GET /api/delta-sync ---
data class DeltaSyncResponse(
    val new_whitelist_entries: List<WhitelistEntryDto>,
    val new_blacklist_entries: List<BlacklistEntryDto>,
    val last_sync_timestamp: Long
)

data class WhitelistEntryDto(
    val domain: String,
    val organisation: String,
    val trust_score: Float
)

data class BlacklistEntryDto(
    val domain: String,
    val confidence: Float,
    val category: String
)
