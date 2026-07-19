package com.fraudshield.app.db

import androidx.room.*
import androidx.lifecycle.LiveData

@Dao
interface SmsLogDao {
    @Insert
    suspend fun insert(entry: SmsLogEntry): Long

    @Query("SELECT * FROM sms_log WHERE syncedToServer = 0")
    suspend fun getUnsynced(): List<SmsLogEntry>

    @Query("UPDATE sms_log SET syncedToServer = 1, jobId = :jobId WHERE id = :id")
    suspend fun markSynced(id: Int, jobId: String)

    @Query("UPDATE sms_log SET serverVerdict = :verdict, verdictSource = 'AI_ML' WHERE jobId = :jobId")
    suspend fun updateServerVerdict(jobId: String, verdict: String)

    @Query("UPDATE sms_log SET userFeedback = :feedback WHERE extractedUrl = :url")
    suspend fun setUserFeedback(url: String, feedback: Int)

    @Query("SELECT * FROM sms_log ORDER BY id DESC LIMIT 50")
    suspend fun getRecent(): List<SmsLogEntry>

    @Query("SELECT * FROM sms_log ORDER BY id DESC")
    fun getAllLive(): LiveData<List<SmsLogEntry>>
}
