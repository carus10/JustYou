package com.justyou.wellness.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import java.time.LocalDate

/**
 * Property 12: Tarih Formatlama
 * Validates: Requirements 13.3
 */
class DateUtilsPropertyTest : FunSpec({
    val turkishMonths = listOf(
        "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
        "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"
    )

    test("Property 12 - Türkçe ay adı ve gün-ay-yıl formatı") {
        checkAll(100, Arb.int(0..3650)) { dayOffset ->
            val date = LocalDate.of(2020, 1, 1).plusDays(dayOffset.toLong())
            val formatted = formatDateTurkish(date)
            val expectedMonth = turkishMonths[date.monthValue - 1]
            formatted shouldContain expectedMonth
            formatted shouldContain date.year.toString()
        }
    }
})
