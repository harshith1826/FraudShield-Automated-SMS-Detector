package com.fraudshield.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [SmsLogEntry::class, UrlCacheEntry::class, SenderAllowlistEntry::class, PatternEntry::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smsLogDao(): SmsLogDao
    abstract fun urlCacheDao(): UrlCacheDao
    abstract fun senderAllowlistDao(): SenderAllowlistDao
    abstract fun patternDao(): PatternDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fraudshield.db"
                ).fallbackToDestructiveMigration() // OK for today's hackathon pace; revisit before production
                    .build()
                INSTANCE = instance
                seedIfEmpty(instance)
                instance
            }
        }

        /**
         * Today's quick-start seed so on-device checks work WITHOUT waiting for
         * Harshit's real blacklist feed or Navya's pattern data.
         * Replace/extend this once delta-sync (Step 10) is wired up.
         */
        private fun seedIfEmpty(db: AppDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                if (db.patternDao().count() == 0) {
                    db.patternDao().insertAll(
                        listOf(
                            PatternEntry(pattern = ".xyz", patternType = "tld", riskScore = 0.9f, category = "high_risk_tld"),
                            PatternEntry(pattern = ".top", patternType = "tld", riskScore = 0.9f, category = "high_risk_tld"),
                            PatternEntry(pattern = ".club", patternType = "tld", riskScore = 0.9f, category = "high_risk_tld"),
                            PatternEntry(pattern = ".tk", patternType = "tld", riskScore = 0.9f, category = "high_risk_tld"),
                            PatternEntry(pattern = "kyc", patternType = "keyword", riskScore = 0.4f, category = "banking_fraud"),
                            PatternEntry(pattern = "verify", patternType = "keyword", riskScore = 0.4f, category = "banking_fraud"),
                            PatternEntry(pattern = "otp", patternType = "keyword", riskScore = 0.5f, category = "banking_fraud"),
                            PatternEntry(pattern = "suspend", patternType = "keyword", riskScore = 0.4f, category = "banking_fraud"),
                            PatternEntry(pattern = "update", patternType = "keyword", riskScore = 0.3f, category = "banking_fraud"),
                            PatternEntry(pattern = "winner", patternType = "keyword", riskScore = 0.5f, category = "lottery_fraud"),
                            PatternEntry(pattern = "prize", patternType = "keyword", riskScore = 0.5f, category = "lottery_fraud")
                        )
                    )
                }
                if (db.senderAllowlistDao().find("VM-SBIBNK") == null) {
                    db.senderAllowlistDao().insertAll(
                        listOf(
                            SenderAllowlistEntry("VM-SBIBNK", "State Bank of India", "bank", true, "sbi.co.in,onlinesbi.sbi"),
                            SenderAllowlistEntry("VM-HDFCBK", "HDFC Bank", "bank", true, "hdfcbank.com")
                        )
                    )
                }
            }
        }
    }
}
