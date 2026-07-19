package com.fraudshield.app.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table 4 — fraud keyword / TLD risk patterns used by Check C (pattern matching).
 * patternType = "keyword" | "tld" | "regex"
 */
@Entity(tableName = "pattern_table")
data class PatternEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pattern: String,
    val patternType: String,
    val riskScore: Float,
    val category: String,
    val language: String = "en"
)
