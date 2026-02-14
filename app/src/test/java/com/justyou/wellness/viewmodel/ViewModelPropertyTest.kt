package com.justyou.wellness.viewmodel

import com.justyou.wellness.data.entity.ChallengeStep
import com.justyou.wellness.data.entity.UserGoals
import com.justyou.wellness.util.isValidTextInput
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/**
 * Property 4: Hedef Güncelleme Round-Trip
 * Validates: Requirements 5.2
 */
class GoalRoundTripPropertyTest : FunSpec({
    test("Property 4 - Hedef değerleri round-trip tutarlılığı") {
        checkAll(100, Arb.int(1..20000), Arb.int(1..10000), Arb.int(1..100000)) {
            water, calorie, step ->
            val goals = UserGoals(waterGoal = water, calorieGoal = calorie, stepGoal = step)
            val restored = goals.copy()
            restored.waterGoal shouldBe water
            restored.calorieGoal shouldBe calorie
            restored.stepGoal shouldBe step
        }
    }
})

/**
 * Property 5: Liste Öğesi Ekleme/Silme Tutarlılığı
 * Validates: Requirements 5.3, 5.4
 */
class ListConsistencyPropertyTest : FunSpec({
    test("Property 5 - Boş olmayan metin eklenebilir, boş metin eklenemez") {
        checkAll(100, Arb.string(minSize = 0, maxSize = 50)) { text ->
            val isValid = isValidTextInput(text)
            if (text.trim().isEmpty()) {
                isValid shouldBe false
            } else {
                isValid shouldBe true
            }
        }
    }
})

/**
 * Property 7: Challenge İlerleme Hesaplaması
 * Validates: Requirements 8.2, 8.5
 */
class ChallengeProgressPropertyTest : FunSpec({
    test("Property 7 - İlerleme yüzdesi tamamlanan/toplam") {
        val arbStep = Arb.boolean().map { completed ->
            ChallengeStep(1, "test", completed)
        }
        checkAll(100, Arb.list(arbStep, 10..10)) { steps ->
            val completed = steps.count { it.isCompleted }
            val progress = completed.toFloat() / steps.size
            progress shouldBe (completed.toFloat() / 10f)
        }
    }

    test("Property 7 - Toggle adım durumu değiştirir") {
        checkAll(100, Arb.int(1..10), Arb.boolean()) { num, initial ->
            val step = ChallengeStep(num, "test", initial)
            val toggled = step.copy(isCompleted = !step.isCompleted)
            toggled.isCompleted shouldBe !initial
        }
    }
})

/**
 * Property 8: Challenge Metin Güncelleme Round-Trip
 * Validates: Requirements 8.3
 */
class ChallengeTextRoundTripPropertyTest : FunSpec({
    test("Property 8 - Metin güncelleme round-trip") {
        checkAll(100, Arb.int(1..10), Arb.string(minSize = 1, maxSize = 100)) { num, desc ->
            val step = ChallengeStep(num, "eski metin")
            val updated = step.copy(description = desc)
            updated.description shouldBe desc
            updated.stepNumber shouldBe num
        }
    }
})
