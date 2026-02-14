package com.justyou.wellness.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val turkishLocale = Locale("tr", "TR")
private val turkishDateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", turkishLocale)

fun formatDateTurkish(date: LocalDate): String = date.format(turkishDateFormatter)
