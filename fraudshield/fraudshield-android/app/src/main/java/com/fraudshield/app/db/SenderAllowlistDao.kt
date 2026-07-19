package com.fraudshield.app.db

import androidx.room.*

@Dao
interface SenderAllowlistDao {
    @Query("SELECT * FROM sender_allowlist WHERE senderId = :senderId LIMIT 1")
    suspend fun find(senderId: String): SenderAllowlistEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<SenderAllowlistEntry>)
}
