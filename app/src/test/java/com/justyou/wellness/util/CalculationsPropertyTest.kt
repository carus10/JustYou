package com.justyou.wellness.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.floats.shouldBeGreaterThanOrEqual
import io.kotest.matchers.floats.shouldBeLessThanOrEqual
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

/**
 * Property 1: Su Seviyesi Hesaplama İnvariantı
 * Validates: Requirements 1.1, 1.2, 1.5
 */
class WaterLevelPropertyTest : FunSpec({
    test("Property 1 - Su seviyesi her zaman 0.0-1.0 arasında") {
        checkAll(100, Arb.int(0..50000), Arb.int(1..20000)) { current, goal ->
            val level = calculateWaterLevel(current, goal)
            level shouldBeGreaterThanOrEqual 0f
            level shouldBeLessThanOrEqual 1f
        }
    }

    test("Property 1 - Hedef aşıldığında seviye 1.0") {
        checkAll(100, Arb.int(1..20000)) { goal ->
            val level = calculateWaterLevel(goal + 1000, goal)
            level shouldBe 1f
        }
    }
})

/**
 * Property 2: Kalori Aşama Hesaplama İnvariantı
 * Validates: Requirements 2.1, 2.4
 */
class BreadStagePropertyTest : FunSpec({
    test("Property 2 - Ekmek aşaması her zaman 0-4 arasında") {
        checkAll(100, Arb.int(0..50000), Arb.int(1..20000)) { current, goal ->
            val stage = calculateBreadStage(current, goal)
            stage shouldBeGreaterThanOrEqual 0
            stage shouldBeLessThanOrEqual 4
        }
    }

    test("Property 2 - Kalori 0 iken aşama 0") {
        checkAll(100, Arb.int(1..20000)) { goal ->
            calculateBreadStage(0, goal) shouldBe 0
        }
    }

    test("Property 2 - Hedef aşıldığında aşama 4") {
        checkAll(100, Arb.int(1..20000)) { goal ->
            calculateBreadStage(goal + 1000, goal) shouldBe 4
        }
    }
})

/**
 * Property 3: Adım Aşama Hesaplama İnvariantı
 * Validates: Requirements 3.1, 3.2, 3.5
 */
class StepStagePropertyTest : FunSpec({
    test("Property 3 - Adım aşaması her zaman 0-3 arasında") {
        checkAll(100, Arb.int(0..200000), Arb.int(1..100000)) { current, goal ->
            val stage = calculateStepStage(current, goal)
            stage shouldBeGreaterThanOrEqual 0
            stage shouldBeLessThanOrEqual 3
        }
    }

    test("Property 3 - Hedefe ulaşıldığında aşama 3") {
        checkAll(100, Arb.int(1..100000)) { goal ->
            calculateStepStage(goal, goal) shouldBe 3
        }
    }
})
