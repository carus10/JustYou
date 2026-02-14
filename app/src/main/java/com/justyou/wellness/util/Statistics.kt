package com.justyou.wellness.util

import com.justyou.wellness.data.entity.DailyTracking

enum class MetricType { WATER, CALORIE, STEP }

enum class TrendIndicator { GREEN_UP, GREEN_DOWN, RED_UP, RED_DOWN }

data class WeeklyStats(
    val avgWater: Int,
    val avgCalorie: Int,
    val avgStep: Int
)

/**
 * 7 günlük ortalama hesaplar. Veri olmayan günler 0 sayılır.
 */
fun calculateWeeklyAverage(data: List<DailyTracking>): WeeklyStats {
    val days = 7
    val totalWater = data.sumOf { it.waterIntake }
    val totalCalorie = data.sumOf { it.calorieIntake }
    val totalStep = data.sumOf { it.stepCount }
    return WeeklyStats(totalWater / days, totalCalorie / days, totalStep / days)
}

/**
 * Belirli gün sayısı için ortalama hesaplar.
 */
fun calculatePeriodAverage(data: List<DailyTracking>, days: Int): WeeklyStats {
    val d = if (days <= 0) 1 else days
    val totalWater = data.sumOf { it.waterIntake }
    val totalCalorie = data.sumOf { it.calorieIntake }
    val totalStep = data.sumOf { it.stepCount }
    return WeeklyStats(totalWater / d, totalCalorie / d, totalStep / d)
}

/**
 * Trend göstergesini belirler.
 * Kalori: hedef aşıldıysa RED_UP (olumsuz), altındaysa GREEN_DOWN (olumlu)
 * Su/Adım: hedefin altındaysa RED_DOWN (olumsuz), ulaşıldıysa GREEN_UP (olumlu)
 */
fun determineTrendIndicator(average: Int, goal: Int, metricType: MetricType): TrendIndicator {
    return when (metricType) {
        MetricType.CALORIE -> {
            if (average > goal) TrendIndicator.RED_UP else TrendIndicator.GREEN_DOWN
        }
        MetricType.WATER, MetricType.STEP -> {
            if (average < goal) TrendIndicator.RED_DOWN else TrendIndicator.GREEN_UP
        }
    }
}
