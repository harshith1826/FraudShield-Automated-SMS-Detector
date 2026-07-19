package com.fraudshield.app.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fraudshield.app.R
import com.fraudshield.app.db.AppDatabase
import com.fraudshield.app.db.SmsLogEntry
import com.fraudshield.app.network.RetrofitClient
import com.fraudshield.app.network.UserFeedbackRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SmsListAdapter(private var items: List<SmsLogEntry>) :
    RecyclerView.Adapter<SmsListAdapter.SmsViewHolder>() {

    class SmsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderText: TextView = view.findViewById(R.id.senderText)
        val urlText: TextView = view.findViewById(R.id.urlText)
        val verdictBadge: TextView = view.findViewById(R.id.verdictBadge)
        val sourceText: TextView = view.findViewById(R.id.sourceText)
        val timeText: TextView = view.findViewById(R.id.timeText)
        val feedbackLayout: LinearLayout = view.findViewById(R.id.feedbackLayout)
        val btnFraud: Button = view.findViewById(R.id.btnFraud)
        val btnSafe: Button = view.findViewById(R.id.btnSafe)
        val feedbackDoneText: TextView = view.findViewById(R.id.feedbackDoneText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sms, parent, false)
        return SmsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val entry = items[position]

        holder.senderText.text = entry.senderId
        holder.urlText.text = entry.extractedUrl

        val finalVerdict = entry.serverVerdict ?: entry.localVerdict
        holder.verdictBadge.text = finalVerdict

        val badgeDrawable = holder.verdictBadge.background as GradientDrawable
        badgeDrawable.setColor(when (finalVerdict) {
            "FRAUD"      -> Color.parseColor("#D32F2F")
            "SUSPICIOUS" -> Color.parseColor("#F9A825")
            "SAFE"       -> Color.parseColor("#388E3C")
            else         -> Color.parseColor("#757575")
        })

        holder.sourceText.text = when (entry.verdictSource) {
            "LOCAL_CACHE"   -> "🔵 Source: Local Database (instant cache)"
            "LOCAL_PATTERN" -> "🔵 Source: Local Pattern Check (no internet needed)"
            "AI_ML"         -> "🟣 Source: AI/ML Analysis (server deep scan)"
            else            -> "⏳ Source: Pending analysis"
        }

        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        holder.timeText.text = sdf.format(Date(entry.timestamp))

        // Show feedback buttons only if user hasn't given feedback yet
        if (entry.userFeedback != null) {
            holder.feedbackLayout.visibility = View.GONE
            holder.feedbackDoneText.visibility = View.VISIBLE
            holder.feedbackDoneText.text = if (entry.userFeedback == 1)
                "✅ You reported this as Fraud"
            else
                "✅ You reported this as Safe"
        } else {
            holder.feedbackLayout.visibility = View.VISIBLE
            holder.feedbackDoneText.visibility = View.GONE
        }

        // Fraud button click
        holder.btnFraud.setOnClickListener {
            sendFeedback(holder, entry, 1)
        }

        // Safe button click
        holder.btnSafe.setOnClickListener {
            sendFeedback(holder, entry, 0)
        }
    }

    private fun sendFeedback(holder: SmsViewHolder, entry: SmsLogEntry, signal: Int) {
        val context = holder.itemView.context
        val deviceId = Settings.Secure.getString(
            context.contentResolver, Settings.Secure.ANDROID_ID
        ) ?: "unknown"

        // Immediately update UI
        holder.feedbackLayout.visibility = View.GONE
        holder.feedbackDoneText.visibility = View.VISIBLE
        holder.feedbackDoneText.text = if (signal == 1)
            "✅ You reported this as Fraud"
        else
            "✅ You reported this as Safe"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)

                // Save feedback to sms_log
                db.smsLogDao().setUserFeedback(entry.extractedUrl, signal)

                // KEY PART: immediately update local cache based on user feedback
                // So next time same URL arrives, it gets instant answer
                val domain = android.net.Uri.parse(entry.extractedUrl).host
                    ?: entry.extractedUrl

                if (signal == 0) {
                    // User said SAFE → add to local whitelist immediately
                    db.urlCacheDao().insert(
                        com.fraudshield.app.db.UrlCacheEntry(
                            url = entry.extractedUrl,
                            domain = domain,
                            status = "whitelist",
                            confidence = 0.85f,
                            category = "user_verified_safe",
                            lastSynced = System.currentTimeMillis()
                        )
                    )
                } else {
                    // User said FRAUD → add to local blacklist immediately
                    db.urlCacheDao().insert(
                        com.fraudshield.app.db.UrlCacheEntry(
                            url = entry.extractedUrl,
                            domain = domain,
                            status = "blacklist",
                            confidence = 0.85f,
                            category = "user_reported_fraud",
                            lastSynced = System.currentTimeMillis()
                        )
                    )
                }

                // Send to Harshit's backend
                RetrofitClient.api.sendFeedback(
                    UserFeedbackRequest(
                        url = entry.extractedUrl,
                        user_signal = signal,
                        current_system_verdict = entry.serverVerdict ?: entry.localVerdict,
                        device_id = deviceId,
                        timestamp = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                // Non-fatal
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<SmsLogEntry>) {
        items = newItems
        notifyDataSetChanged()
    }
}