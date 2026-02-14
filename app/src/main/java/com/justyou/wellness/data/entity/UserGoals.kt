package com.justyou.wellness.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_goals")
data class UserGoals(
    @PrimaryKey val id: Int = 1, // Tek kayıt
    val waterGoal: Int = 2000,   // ml
    val calorieGoal: Int = 2000, // kcal
    val stepGoal: Int = 10000    // adım
)
