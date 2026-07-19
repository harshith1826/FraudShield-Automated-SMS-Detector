package com.fraudshield.app.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Table 3 — registered legitimate sender IDs (e.g. "VM-SBIBNK" -> SBI).
 * associatedDomains is comma-separated, e.g. "sbi.co.in,onlinesbi.sbi"
 */
@Entity(tableName = "sender_allowlist")
data class SenderAllowlistEntry(
    @PrimaryKey val senderId: String,
    val organisation: String,
    val category: String,
    val verified: Boolean,
    val associatedDomains: String
)
