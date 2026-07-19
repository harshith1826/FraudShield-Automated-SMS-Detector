package com.fraudshield.app.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fraudshield.app.R

object NotificationHelper {
    private const val CHANNEL_ID = "fraudshield_verdicts"
    const val ACTION_FEEDBACK_FRAUD = "com.fraudshield.app.FEEDBACK_FRAUD"
    const val ACTION_FEEDBACK_SAFE = "com.fraudshield.app.FEEDBACK_SAFE"
    const val EXTRA_URL = "extra_url"
    const val EXTRA_VERDICT = "extra_verdict"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Fraud Warnings", NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * state: "PENDING" (grey) | "SUSPICIOUS" (yellow) | "FRAUD" (red) | "SAFE" (green)
     * notificationId should be stable per-SMS so later calls UPDATE the same
     * notification instead of creating a new one (grey -> yellow -> red).
     */
    fun show(context: Context, notificationId: Int, state: String, url: String, message: String) {
        ensureChannel(context)

        val (icon, title) = when (state) {
            "FRAUD" -> android.R.drawable.stat_sys_warning to "🔴 Fraud detected — do not click"
            "SUSPICIOUS" -> android.R.drawable.stat_sys_warning to "⚠️ Suspicious signals found"
            "SAFE" -> android.R.drawable.stat_notify_sync to "✅ Verified safe"
            else -> android.R.drawable.stat_notify_sync to "⏳ Verifying link..."
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)

        // Only show feedback buttons once we have an actual verdict (not on the grey "verifying" stage)
        if (state == "FRAUD" || state == "SUSPICIOUS" || state == "SAFE") {
            builder.addAction(0, "Yes, This is Fraud", actionIntent(context, ACTION_FEEDBACK_FRAUD, url, state, notificationId))
            builder.addAction(0, "This is Safe", actionIntent(context, ACTION_FEEDBACK_SAFE, url, state, notificationId))
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(notificationId, builder.build())
    }

    private fun actionIntent(context: Context, action: String, url: String, verdict: String, notificationId: Int): PendingIntent {
        val intent = Intent(context, FeedbackActionReceiver::class.java).apply {
            this.action = action
            putExtra(EXTRA_URL, url)
            putExtra(EXTRA_VERDICT, verdict)
        }
        return PendingIntent.getBroadcast(
            context, notificationId * 10 + action.hashCode() % 10, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
