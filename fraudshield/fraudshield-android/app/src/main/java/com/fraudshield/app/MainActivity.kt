package com.fraudshield.app

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fraudshield.app.ui.SmsListActivity
import com.fraudshield.app.workers.SyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val requestPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val allGranted = results.values.all { it }
        statusText?.text = if (allGranted)
            "✅ Permissions granted. FraudShield is now monitoring SMS in the background."
        else
            "⚠️ SMS permission is required for FraudShield to work. Please grant it in Settings."
    }

    private var statusText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 96, 48, 48)
        }

        statusText = TextView(this).apply {
            text = "Requesting permissions..."
            textSize = 16f
        }

        val viewMessagesButton = Button(this).apply {
            text = "📋 View All Detected Messages"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, SmsListActivity::class.java))
            }
        }

        layout.addView(statusText)
        layout.addView(viewMessagesButton)
        setContentView(layout)

        requestPermissions.launch(
            arrayOf(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.POST_NOTIFICATIONS
            )
        )

        scheduleBackgroundSync()
    }

    private fun scheduleBackgroundSync() {
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "fraudshield_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}