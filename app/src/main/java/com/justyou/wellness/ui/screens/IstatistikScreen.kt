package com.justyou.wellness.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.data.entity.DailyTracking
import com.justyou.wellness.data.entity.UserGoals
import com.justyou.wellness.ui.theme.*
import com.justyou.wellness.viewmodel.CalendarMetric
import com.justyou.wellness.viewmodel.IstatistikViewModel
import com.justyou.wellness.viewmodel.TimeRange
import java.time.DayOfWeek
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IstatistikScreen(viewModel: IstatistikViewModel) {
    val state by viewModel.state.collectAsState()
    var timeExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("İstatistikler", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundPrimary)
            )
        },
        containerColor = BackgroundPrimary
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Zaman aralığı dropdown
            Box {
                OutlinedButton(
                    onClick = { timeExpanded = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(state.selectedRange.label, fontSize = 14.sp, color = TextPrimary, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextPrimary)
                }
                DropdownMenu(expanded = timeExpanded, onDismissRequest = { timeExpanded = false }) {
                    TimeRange.entries.forEach { range ->
                        DropdownMenuItem(
                            text = { Text(range.label) },
                            onClick = { viewModel.setTimeRange(range); timeExpanded = false }
                        )
                    }
                }
            }

            // Grafik kartı - hedef değerlere göre normalize edilmiş
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        LegendItem(Color(0xFFD4A574), "Kalori")
                        Spacer(modifier = Modifier.width(16.dp))
                        LegendItem(PastelBlue, "Su")
                        Spacer(modifier = Modifier.width(16.dp))
                        LegendItem(Color(0xFF2F2F2F), "Adım")
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    val goals = state.goals
                    Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                        val w = size.width; val h = size.height
                        for (i in 0..4) { drawLine(Color(0xFFE0E0E0), Offset(0f, h * i / 4), Offset(w, h * i / 4), 1f) }
                        val data = state.trackingData
                        if (data.isNotEmpty()) {
                            val calGoal = goals.calorieGoal.toFloat().coerceAtLeast(1f)
                            val watGoal = goals.waterGoal.toFloat().coerceAtLeast(1f)
                            val stpGoal = goals.stepGoal.toFloat().coerceAtLeast(1f)

                            val textPaint = android.graphics.Paint().apply {
                                color = android.graphics.Color.DKGRAY
                                textSize = 24f
                                textAlign = android.graphics.Paint.Align.CENTER
                                isAntiAlias = true
                            }

                            if (data.size == 1) {
                                val d = data[0]
                                val barW = w / 5
                                val chartH = h * 0.8f

                                // Hedef değerlere göre normalize et, max %120 göster
                                val calH = ((d.calorieIntake / calGoal).coerceIn(0f, 1.2f)) * chartH
                                val watH = ((d.waterIntake / watGoal).coerceIn(0f, 1.2f)) * chartH
                                val stpH = ((d.stepCount / stpGoal).coerceIn(0f, 1.2f)) * chartH

                                val calX = w * 0.15f; val watX = w * 0.4f; val stpX = w * 0.65f

                                if (calH > 0) drawRoundRect(Color(0xFFD4A574), Offset(calX, h - calH), Size(barW, calH), CornerRadius(6f))
                                if (watH > 0) drawRoundRect(PastelBlue, Offset(watX, h - watH), Size(barW, watH), CornerRadius(6f))
                                if (stpH > 0) drawRoundRect(Color(0xFF2F2F2F), Offset(stpX, h - stpH), Size(barW, stpH), CornerRadius(6f))

                                drawContext.canvas.nativeCanvas.apply {
                                    if (d.calorieIntake > 0) drawText("${d.calorieIntake}", calX + barW / 2, h - calH - 6, textPaint)
                                    if (d.waterIntake > 0) drawText("${d.waterIntake}", watX + barW / 2, h - watH - 6, textPaint)
                                    if (d.stepCount > 0) drawText("${d.stepCount}", stpX + barW / 2, h - stpH - 6, textPaint)
                                }
                            } else {
                                // Çoklu gün: hedef değerlere göre normalize
                                drawCurve(w, h, data.map { 1f - (it.calorieIntake / calGoal).coerceIn(0f, 1.2f) / 1.2f }, Color(0xFFD4A574), data.size)
                                drawCurve(w, h, data.map { 1f - (it.waterIntake / watGoal).coerceIn(0f, 1.2f) / 1.2f }, PastelBlue, data.size)
                                drawCurve(w, h, data.map { 1f - (it.stepCount / stpGoal).coerceIn(0f, 1.2f) / 1.2f }, Color(0xFF2F2F2F), data.size)
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Miktar", fontSize = 10.sp, color = TextMuted)
                        Text("Zaman", fontSize = 10.sp, color = TextMuted)
                    }
                }
            }

            // Takvim kartı
            CalendarCard(
                calendarMetric = state.calendarMetric,
                calendarMonth = state.calendarMonth,
                calendarData = state.calendarData,
                goals = state.goals,
                onMetricChange = { viewModel.setCalendarMetric(it) },
                onPreviousMonth = { viewModel.previousMonth() },
                onNextMonth = { viewModel.nextMonth() }
            )
        }
    }
}

@Composable
private fun CalendarCard(
    calendarMetric: CalendarMetric,
    calendarMonth: YearMonth,
    calendarData: Map<Int, DailyTracking>,
    goals: UserGoals,
    onMetricChange: (CalendarMetric) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    var metricExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Üst satır: metrik seçici + ay navigasyonu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Metrik dropdown (sol üst)
                Box {
                    OutlinedButton(
                        onClick = { metricExpanded = true },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(calendarMetric.label, fontSize = 13.sp, color = TextPrimary)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                    }
                    DropdownMenu(expanded = metricExpanded, onDismissRequest = { metricExpanded = false }) {
                        CalendarMetric.entries.forEach { metric ->
                            DropdownMenuItem(
                                text = { Text(metric.label) },
                                onClick = { onMetricChange(metric); metricExpanded = false }
                            )
                        }
                    }
                }

                // Ay navigasyonu
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPreviousMonth, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Önceki ay", tint = TextPrimary)
                    }
                    val monthNames = listOf("", "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
                        "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık")
                    Text(
                        "${monthNames[calendarMonth.monthValue]} ${calendarMonth.year}",
                        fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary
                    )
                    IconButton(onClick = onNextMonth, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Sonraki ay", tint = TextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Gün başlıkları
            val dayLabels = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
            Row(modifier = Modifier.fillMaxWidth()) {
                dayLabels.forEach { label ->
                    Text(
                        label, fontSize = 11.sp, color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))

            // Takvim grid
            val firstDay = calendarMonth.atDay(1)
            // Pazartesi=1 ... Pazar=7 -> offset = dayOfWeek - 1
            val startOffset = firstDay.dayOfWeek.value - 1
            val daysInMonth = calendarMonth.lengthOfMonth()
            val totalCells = startOffset + daysInMonth
            val rows = (totalCells + 6) / 7

            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (col in 0..6) {
                        val cellIndex = row * 7 + col
                        val dayNum = cellIndex - startOffset + 1
                        if (dayNum in 1..daysInMonth) {
                            val tracking = calendarData[dayNum]
                            val color = getDayColor(tracking, calendarMetric, goals)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$dayNum", fontSize = 12.sp,
                                    color = if (color == Color(0xFFE0E0E0)) TextMuted else Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Renk açıklamaları
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                val (c1, l1, c2, l2) = when (calendarMetric) {
                    CalendarMetric.KALORI -> listOf(Color(0xFFFFD54F), "Hedefte", Color(0xFFE53935), "Aşıldı")
                    CalendarMetric.SU -> listOf(Color(0xFF42A5F5), "Hedefte", Color(0xFFE53935), "Eksik")
                    CalendarMetric.ADIM -> listOf(Color(0xFF66BB6A), "Hedefte", Color(0xFFE53935), "Eksik")
                }
                CalendarLegend(c1 as Color, l1 as String)
                Spacer(modifier = Modifier.width(12.dp))
                CalendarLegend(c2 as Color, l2 as String)
                Spacer(modifier = Modifier.width(12.dp))
                CalendarLegend(Color(0xFFE0E0E0), "Veri yok")
            }
        }
    }
}

private fun getDayColor(tracking: DailyTracking?, metric: CalendarMetric, goals: UserGoals): Color {
    if (tracking == null) return Color(0xFFE0E0E0) // Gri - veri yok

    return when (metric) {
        CalendarMetric.KALORI -> {
            val value = tracking.calorieIntake
            if (value == 0) Color(0xFFE0E0E0) // Veri girilmemiş
            else if (value <= goals.calorieGoal) Color(0xFFFFD54F) // Sarı - hedefte veya altında
            else Color(0xFFE53935) // Kırmızı - aşıldı
        }
        CalendarMetric.SU -> {
            val value = tracking.waterIntake
            if (value == 0) Color(0xFFE0E0E0)
            else if (value >= goals.waterGoal) Color(0xFF42A5F5) // Mavi - hedefte veya üstünde
            else Color(0xFFE53935) // Kırmızı - eksik
        }
        CalendarMetric.ADIM -> {
            val value = tracking.stepCount
            if (value == 0) Color(0xFFE0E0E0)
            else if (value >= goals.stepGoal) Color(0xFF66BB6A) // Yeşil - hedefte veya üstünde
            else Color(0xFFE53935) // Kırmızı - eksik
        }
    }
}

@Composable
private fun CalendarLegend(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = TextMuted)
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(10.dp)) { drawCircle(color) }
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 11.sp, color = TextSecondary)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCurve(w: Float, h: Float, yValues: List<Float>, color: Color, points: Int) {
    if (points < 2) return
    val path = Path()
    for (i in yValues.indices) {
        val x = w * i / (points - 1); val y = h * yValues[i].coerceIn(0f, 1f)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    drawPath(path, color, style = Stroke(width = 3f))
}
