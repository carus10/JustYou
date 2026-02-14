package com.justyou.wellness.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_tracking")
data class DailyTracking(
    @PrimaryKey val date: String, // "yyyy-MM-dd" formatı
    val waterIntake: Int = 0,     // ml cinsinden
    val calorieIntake: Int = 0,   // kcal cinsinden
    val stepCount: Int = 0        // adım sayısı
)
