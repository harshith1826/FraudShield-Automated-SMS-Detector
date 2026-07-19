package com.fraudshield.app.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms_log")
data class SmsLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderId: String,
    val smsBody: String,
    val extractedUrl: String,
    val timestamp: Long,
    val localVerdict: String,
    val syncedToServer: Boolean = false,
    val jobId: String? = null,
    val serverVerdict: String? = null,
    val verdictSource: String = "PENDING",
    val userFeedback: Int? = null
)