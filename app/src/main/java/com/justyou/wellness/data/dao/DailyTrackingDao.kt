package com.justyou.wellness.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.justyou.wellness.data.entity.DailyTracking
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyTrackingDao {
    @Query("SELECT * FROM daily_tracking WHERE date = :date")
    fun getByDate(date: String): Flow<DailyTracking?>

    @Query("SELECT * FROM daily_tracking WHERE date = :date")
    suspend fun getByDateOnce(date: String): DailyTracking?

    @Query("SELECT * FROM daily_tracking WHERE date BETWEEN :startDate AND :endDate ORDER BY date")
    fun getRange(startDate: String, endDate: String): Flow<List<DailyTracking>>

    @Upsert
    suspend fun upsert(tracking: DailyTracking)
}
