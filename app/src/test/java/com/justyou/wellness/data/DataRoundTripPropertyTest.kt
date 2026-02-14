package com.justyou.wellness.data

import com.justyou.wellness.data.entity.Affirmation
import com.justyou.wellness.data.entity.ChallengeStep
import com.justyou.wellness.data.entity.DailyTracking
import com.justyou.wellness.data.entity.NegativeThought
import com.justyou.wellness.data.entity.UserGoals
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/**
 * Property 11: Veri Saklama Round-Trip
 * Feature: wellness-android-app, Property 11: Veri Saklama Round-Trip
 * Validates: Requirements 12.1, 12.2, 12.3, 12.4, 12.6
 */
class DataRoundTripPropertyTest : FunSpec({

    test("Property 11 - DailyTracking round-trip") {
        val arbDate = Arb.int(1..3650).map { dayOffset ->
            java.time.LocalDate.of(2020, 1, 1).plusDays(dayOffset.toLong())
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        checkAll(100, arbDate, Arb.int(0..10000), Arb.int(0..10000), Arb.int(0..100000)) {
            date, water, calorie, step ->
            val original = DailyTracking(date, water, calorie, step)
            val copied = original.copy()
            copied shouldBe original
            copied.date shouldBe date
            copied.waterIntake shouldBe water
            copied.calorieIntake shouldBe calorie
            copied.stepCount shouldBe step
        }
    }

    test("Property 11 - UserGoals round-trip") {
        checkAll(100, Arb.int(1..20000), Arb.int(1..10000), Arb.int(1..100000)) {
            waterGoal, calorieGoal, stepGoal ->
            val original = UserGoals(waterGoal = waterGoal, calorieGoal = calorieGoal, stepGoal = stepGoal)
            val copied = original.copy()
            copied shouldBe original
            copied.waterGoal shouldBe waterGoal
            copied.calorieGoal shouldBe calorieGoal
            copied.stepGoal shouldBe stepGoal
        }
    }

    test("Property 11 - Affirmation round-trip") {
        checkAll(
            100,
            Arb.long(1L..10000L),
            Arb.string(minSize = 1, maxSize = 50),
            Arb.long(0L..1000000000L)
        ) { id, text, createdAt ->
            val original = Affirmation(id = id, text = text, createdAt = createdAt)
            val copied = original.copy()
            copied shouldBe original
            copied.text shouldBe text
        }
    }

    test("Property 11 - NegativeThought round-trip") {
        checkAll(
            100,
            Arb.long(1L..10000L),
            Arb.string(minSize = 1, maxSize = 50),
            Arb.long(0L..1000000000L)
        ) { id, text, createdAt ->
            val original = NegativeThought(id = id, text = text, createdAt = createdAt)
            val copied = original.copy()
            copied shouldBe original
            copied.text shouldBe text
        }
    }

    test("Property 11 - ChallengeStep round-trip") {
        checkAll(100, Arb.int(1..10), Arb.string(minSize = 1, maxSize = 50), Arb.boolean()) {
            stepNumber, description, isCompleted ->
            val original = ChallengeStep(stepNumber, description, isCompleted)
            val copied = original.copy()
            copied shouldBe original
            copied.stepNumber shouldBe stepNumber
            copied.description shouldBe description
            copied.isCompleted shouldBe isCompleted
        }
    }
})
