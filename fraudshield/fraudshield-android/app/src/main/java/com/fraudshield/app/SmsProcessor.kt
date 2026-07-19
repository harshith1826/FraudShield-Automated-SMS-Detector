package com.fraudshield.app

import android.content.Context
import android.provider.Settings
import com.fraudshield.app.db.AppDatabase
import com.fraudshield.app.db.SmsLogEntry
import com.fraudshield.app.network.IngestRequest
import com.fraudshield.app.network.RetrofitClient
import com.fraudshield.app.ui.NotificationHelper

/**
 * The full pipeline for one incoming SMS, matching the optimistic UI timeline:
 *   0ms   -> grey "Verifying link..."
 *   ~50ms -> yellow "Suspicious signals" (after on-device checks)
 *   ~300ms+ -> red/green (after server responds)
 */
object SmsProcessor {

    suspend fun process(context: Context, senderId: String, smsBody: String, url: String) {
        val db = AppDatabase.getInstance(context)
        val notificationId = url.hashCode()
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"

        // 0ms — grey, "verifying"
        NotificationHelper.show(context, notificationId, "PENDING", url, "Checking this link from $senderId...")

        // ~50ms — on-device checks (Check A/B/C)
        val onDeviceResult = OnDeviceChecker.check(db, senderId, url)

        // Save to sms_log immediately so nothing is lost even if network fails
        val logId = db.smsLogDao().insert(
            SmsLogEntry(
                senderId = senderId,
                smsBody = smsBody,
                extractedUrl = url,
                timestamp = System.currentTimeMillis(),
                localVerdict = onDeviceResult.verdict
            )
        )

        // If on-device cache already gave a DEFINITIVE answer, show it now, no need to wait on server
        if (onDeviceResult.verdict == "SAFE" || onDeviceResult.verdict == "FRAUD") {
            val msg = if (onDeviceResult.verdict == "FRAUD")
                "This link matches a known fraud pattern. Do not click."
            else
                "This is a verified safe link."
            NotificationHelper.show(context, notificationId, onDeviceResult.verdict, url, msg)
            return
        }

        // Otherwise show the intermediate yellow state while we wait on the server
        if (onDeviceResult.verdict == "SUSPICIOUS") {
            NotificationHelper.show(
                context, notificationId, "SUSPICIOUS", url,
                "Suspicious signals found (${onDeviceResult.flagsRaised.joinToString(", ")}). Checking further..."
            )
        }

        // ~300ms+ — call backend for real verdict
        try {
            val ingestResponse = RetrofitClient.api.ingest(
                IngestRequest(
                    sender_id = senderId,
                    sms_body = smsBody,
                    extracted_url = url,
                    timestamp = System.currentTimeMillis(),
                    device_id = deviceId
                )
            )
            db.smsLogDao().markSynced(logId.toInt(), ingestResponse.job_id)

            // Poll once immediately; WorkManager (Step 10) will keep polling later if not ready yet
            val verdict = try {
                RetrofitClient.api.getVerdict(ingestResponse.job_id)
            } catch (e: Exception) {
                null
            }

            if (verdict != null) {
                db.smsLogDao().updateServerVerdict(ingestResponse.job_id, verdict.verdict)
                val message = verdict.warning_text_english
                    ?: verdict.specific_threat
                    ?: "Verdict: ${verdict.verdict}"
                NotificationHelper.show(context, notificationId, verdict.verdict, url, message)
            }
            // If verdict is null (job still processing), leave the SUSPICIOUS/PENDING
            // notification up — WorkManager's periodic poll will update it later.

        } catch (e: Exception) {
            // Backend unreachable (no base URL set yet, or Harshit's server down).
            // Keep whatever on-device verdict we already showed; don't crash.
        }
    }
}
