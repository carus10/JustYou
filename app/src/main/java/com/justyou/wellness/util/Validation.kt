package com.justyou.wellness.util

fun isValidTextInput(text: String): Boolean = text.trim().isNotEmpty()

fun isValidGoalValue(value: Int): Boolean = value > 0
