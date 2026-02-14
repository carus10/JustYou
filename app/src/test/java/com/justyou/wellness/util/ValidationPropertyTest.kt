package com.justyou.wellness.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.of
import io.kotest.property.checkAll

/**
 * Property 6: Boş Giriş Doğrulaması
 * Validates: Requirements 5.7
 */
class ValidationPropertyTest : FunSpec({
    test("Property 6 - Boş veya sadece boşluk metinler reddedilmeli") {
        val arbBlank = Arb.of("", " ", "  ", "\t", "\n", " \t\n ", "   \t  ")
        checkAll(100, arbBlank) { text ->
            isValidTextInput(text) shouldBe false
        }
    }

    test("Property 6 - Boş olmayan metinler kabul edilmeli") {
        val arbValid = Arb.of("merhaba", " test ", "a", "Olumlu düşünce")
        checkAll(100, arbValid) { text ->
            isValidTextInput(text) shouldBe true
        }
    }
})
