package com.justyou.wellness.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justyou.wellness.data.entity.NegativeThought
import kotlinx.coroutines.flow.Flow

@Dao
interface NegativeThoughtDao {
    @Query("SELECT * FROM negative_thoughts ORDER BY createdAt DESC")
    fun getAll(): Flow<List<NegativeThought>>

    @Query("SELECT * FROM negative_thoughts ORDER BY createdAt DESC")
    suspend fun getAllOnce(): List<NegativeThought>

    @Insert
    suspend fun insert(thought: NegativeThought)

    @Delete
    suspend fun delete(thought: NegativeThought)
}
