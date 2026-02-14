package com.justyou.wellness.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_steps")
data class ChallengeStep(
    @PrimaryKey val stepNumber: Int, // 1-10
    val description: String,
    val isCompleted: Boolean = false
)
