package com.justyou.wellness.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.justyou.wellness.data.entity.UserGoals
import kotlinx.coroutines.flow.Flow

@Dao
interface UserGoalsDao {
    @Query("SELECT * FROM user_goals WHERE id = 1")
    fun getGoals(): Flow<UserGoals?>

    @Upsert
    suspend fun upsert(goals: UserGoals)
}
