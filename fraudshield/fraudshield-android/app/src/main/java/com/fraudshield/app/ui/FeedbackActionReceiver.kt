package com.fraudshield.app.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.fraudshield.app.db.AppDatabase
import com.fraudshield.app.network.RetrofitClient
import com.fraudshield.app.network.UserFeedbackRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Step 9 — user feedback buttons.
 * Tapping a button on the verdict notification sends signal 1 (fraud) or 0 (safe)
 * to POST /api/userfeedback, per the locked API contract.
 */
class FeedbackActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val url = intent.getStringExtra(NotificationHelper.EXTRA_URL) ?: return
        val currentVerdict = intent.getStringExtra(NotificationHelper.EXTRA_VERDICT) ?: "UNKNOWN"
        val signal = when (intent.action) {
            NotificationHelper.ACTION_FEEDBACK_FRAUD -> 1
            NotificationHelper.ACTION_FEEDBACK_SAFE -> 0
            else -> return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"
                val db = AppDatabase.getInstance(context)
                db.smsLogDao().setUserFeedback(url, signal)

                try {
                    RetrofitClient.api.sendFeedback(
                        UserFeedbackRequest(
                            url = url,
                            user_signal = signal,
                            current_system_verdict = currentVerdict,
                            device_id = deviceId,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                } catch (e: Exception) {
                    // Backend might not have /api/userfeedback live yet today — fail silently,
                    // WorkManager sync can retry later if you add a retry queue.
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
