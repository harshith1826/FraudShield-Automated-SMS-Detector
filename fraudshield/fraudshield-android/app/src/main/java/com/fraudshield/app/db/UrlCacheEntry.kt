package com.fraudshield.app.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table 2 — whitelist + blacklist cached on device for instant offline lookup.
 * status = "whitelist" | "blacklist" | "pending"
 */
@Entity(tableName = "url_cache")
data class UrlCacheEntry(
    @PrimaryKey val url: String,
    val domain: String,
    val status: String,
    val confidence: Float,
    val category: String?,
    val lastSynced: Long
)
