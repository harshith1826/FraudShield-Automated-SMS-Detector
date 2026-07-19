package com.fraudshield.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.provider.Telephony.SMS_RECEIVED") return

        val bundle = intent.extras ?: return
        val pdus = bundle.get("pdus") as? Array<*> ?: return
        val format = bundle.getString("format")

        val bodyBuilder = StringBuilder()
        var senderId = ""

        for (pdu in pdus) {
            val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
            bodyBuilder.append(sms.messageBody)
            senderId = sms.originatingAddress ?: senderId
        }

        val body = bodyBuilder.toString()
        val url = UrlExtractor.extract(body) ?: return // Edge case: no URL in SMS -> ignore silently

        // goAsync() extends the receiver's lifetime beyond onReceive() so we have
        // time to hit Room + the network without the OS killing us mid-work.
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                SmsProcessor.process(context, senderId, body, url)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
