package com.justyou.wellness.util

import com.justyou.wellness.data.entity.DailyTracking
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

/**
 * Property 9: Haftalık İstatistik Hesaplaması
 * Validates: Requirements 9.1, 9.5
 */
class WeeklyStatsPropertyTest : FunSpec({
    test("Property 9 - Haftalık ortalama toplam/7 ye eşit") {
        val arbTracking = Arb.int(0..5000).map { w ->
            DailyTracking("2025-01-01", w, w, w * 10)
        }
        checkAll(100, Arb.list(arbTracking, 0..7)) { data ->
            val stats = calculateWeeklyAverage(data)
            val expectedWater = data.sumOf { it.waterIntake } / 7
            val expectedCalorie = data.sumOf { it.calorieIntake } / 7
            val expectedStep = data.sumOf { it.stepCount } / 7
            stats.avgWater shouldBe expectedWater
            stats.avgCalorie shouldBe expectedCalorie
            stats.avgStep shouldBe expectedStep
        }
    }
})

/**
 * Property 10: Trend Göstergesi Hesaplaması
 * Validates: Requirements 9.2, 9.3, 9.4
 */
class TrendIndicatorPropertyTest : FunSpec({
    test("Property 10 - Kalori hedefi aşıldığında RED_UP") {
        checkAll(100, Arb.int(1..10000)) { goal ->
            determineTrendIndicator(goal + 1, goal, MetricType.CALORIE) shouldBe TrendIndicator.RED_UP
        }
    }

    test("Property 10 - Su hedefin altında RED_DOWN") {
        checkAll(100, Arb.int(2..10000)) { goal ->
            determineTrendIndicator(goal - 1, goal, MetricType.WATER) shouldBe TrendIndicator.RED_DOWN
        }
    }

    test("Property 10 - Adım hedefe ulaşıldığında GREEN_UP") {
        checkAll(100, Arb.int(1..100000)) { goal ->
            determineTrendIndicator(goal, goal, MetricType.STEP) shouldBe TrendIndicator.GREEN_UP
        }
    }
})
