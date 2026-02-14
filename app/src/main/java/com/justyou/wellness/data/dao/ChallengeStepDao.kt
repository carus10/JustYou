package com.justyou.wellness.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.justyou.wellness.data.entity.ChallengeStep
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeStepDao {
    @Query("SELECT * FROM challenge_steps ORDER BY stepNumber")
    fun getAll(): Flow<List<ChallengeStep>>

    @Upsert
    suspend fun upsert(step: ChallengeStep)
}
