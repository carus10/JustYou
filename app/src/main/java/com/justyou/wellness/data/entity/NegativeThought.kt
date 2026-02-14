package com.justyou.wellness.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "negative_thoughts")
data class NegativeThought(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val response: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
