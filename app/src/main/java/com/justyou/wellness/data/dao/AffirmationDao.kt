package com.justyou.wellness.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justyou.wellness.data.entity.Affirmation
import kotlinx.coroutines.flow.Flow

@Dao
interface AffirmationDao {
    @Query("SELECT * FROM affirmations ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Affirmation>>

    @Query("SELECT * FROM affirmations")
    suspend fun getAllOnce(): List<Affirmation>

    @Insert
    suspend fun insert(affirmation: Affirmation)

    @Delete
    suspend fun delete(affirmation: Affirmation)
}
