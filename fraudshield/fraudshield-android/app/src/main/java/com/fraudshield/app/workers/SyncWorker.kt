package com.fraudshield.app.workers

import android.content.Context
import android.provider.Settings
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fraudshield.app.db.AppDatabase
import com.fraudshield.app.db.UrlCacheEntry
import com.fraudshield.app.network.IngestRequest
import com.fraudshield.app.network.RetrofitClient
import com.fraudshield.app.ui.NotificationHelper

/**
 * Step 10 — every 15 minutes:
 *  1. Push any unsynced SMS rows to /api/ingest (in case the live call in
 *     SmsProcessor failed, e.g. no internet at the time the SMS arrived)
 *  2. Poll /api/getmlfeedback for any rows whose deep-analysis verdict
 *     wasn't ready yet on first try
 *  3. Pull /api/delta-sync to refresh the local whitelist/blacklist cache
 */
class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getInstance(applicationContext)
        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"
        val api = RetrofitClient.api

        return try {
            // 1. Sync unsent rows
            val unsynced = db.smsLogDao().getUnsynced()
            for (entry in unsynced) {
                val response = api.ingest(
                    IngestRequest(
                        sender_id = entry.senderId,
                        sms_body = entry.smsBody,
                        extracted_url = entry.extractedUrl,
                        timestamp = entry.timestamp,
                        device_id = deviceId
                    )
                )
                db.smsLogDao().markSynced(entry.id, response.job_id)
            }

            // 2. Poll pending verdicts for rows that have a jobId but no server verdict yet
            val recent = db.smsLogDao().getRecent()
            recent.filter { it.jobId != null && it.serverVerdict == null }.forEach { entry ->
                try {
                    val verdict = api.getVerdict(entry.jobId!!)
                    db.smsLogDao().updateServerVerdict(entry.jobId, verdict.verdict)
                    NotificationHelper.show(
                        applicationContext, entry.extractedUrl.hashCode(), verdict.verdict,
                        entry.extractedUrl, verdict.warning_text_english ?: verdict.specific_threat ?: ""
                    )
                } catch (e: Exception) {
                    // job not ready yet, try again next cycle
                }
            }

            // 3. Delta sync — refresh local whitelist/blacklist cache
            try {
                val delta = api.deltaSync()
                val newEntries = delta.new_whitelist_entries.map {
                    UrlCacheEntry(it.domain, it.domain, "whitelist", it.trust_score, "whitelisted", delta.last_sync_timestamp)
                } + delta.new_blacklist_entries.map {
                    UrlCacheEntry(it.domain, it.domain, "blacklist", it.confidence, it.category, delta.last_sync_timestamp)
                }
                db.urlCacheDao().insertAll(newEntries)
            } catch (e: Exception) {
                // delta-sync endpoint might not be live yet today — non-fatal
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
