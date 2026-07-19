package com.fraudshield.app.db

import androidx.room.*

@Dao
interface UrlCacheDao {
    @Query("SELECT * FROM url_cache WHERE url = :url LIMIT 1")
    suspend fun findByUrl(url: String): UrlCacheEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<UrlCacheEntry>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: UrlCacheEntry)

    @Query("SELECT MAX(lastSynced) FROM url_cache")
    suspend fun getLastSyncTime(): Long?
}
