package com.fraudshield.app.db

import androidx.room.*

@Dao
interface PatternDao {
    @Query("SELECT * FROM pattern_table")
    suspend fun getAll(): List<PatternEntry>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entries: List<PatternEntry>)

    @Query("SELECT COUNT(*) FROM pattern_table")
    suspend fun count(): Int
}
