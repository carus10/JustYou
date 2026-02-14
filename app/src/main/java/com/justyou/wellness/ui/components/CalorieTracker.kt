package com.justyou.wellness.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.ui.theme.*

@Composable
fun CalorieTracker(
    currentCalories: Int,
    goalCalories: Int,
    onAddCalories: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val ratio = if (goalCalories > 0) currentCalories.toFloat() / goalCalories else 0f
    val level by animateFloatAsState(
        targetValue = ratio.coerceIn(0f, 1.2f),
        animationSpec = tween(600), label = "calorieLevel"
    )
    val fillColor = when {
        ratio > 1f -> Color(0xFFEF5350)
        ratio >= 0.95f -> Color(0xFF42A5F5)
        else -> Color(0xFFFFCA28)
    }

    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Kalori Ekle") },
            text = {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it.filter { c -> c.isDigit() } },
                    label = { Text("Kalori (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    inputText.toIntOrNull()?.let { if (it > 0) onAddCalories(it) }
                    inputText = ""; showDialog = false
                }) { Text("Ekle") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; inputText = "" }) { Text("İptal") }
            }
        )
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Kalori Takip", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            Canvas(modifier = Modifier.size(70.dp, 100.dp)) {
                drawHumanSilhouette(level.coerceIn(0f, 1f), fillColor)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text("Alınan: $currentCalories kcal\nHedef: $goalCalories kcal", fontSize = 10.sp, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonGradientStart),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) { Text("+ Kalori Gir", fontSize = 12.sp, color = TextPrimary) }
        }
    }
}

private fun DrawScope.drawHumanSilhouette(fillLevel: Float, fillColor: Color) {
    val w = size.width; val h = size.height
    val cx = w * 0.5f
    val outlineColor = Color(0xFF8A9BA8)
    val glassColor = Color(0xFFD8E2E8)

    // Tam vücut insan silüeti - görseldeki gibi kollar açık, ayakta duran
    val silhouette = Path().apply {
        // Kafa üstü
        moveTo(cx, h * 0.02f)
        // Kafa sağ taraf
        cubicTo(cx + w * 0.1f, h * 0.02f, cx + w * 0.12f, h * 0.04f, cx + w * 0.12f, h * 0.07f)
        cubicTo(cx + w * 0.12f, h * 0.1f, cx + w * 0.1f, h * 0.12f, cx + w * 0.06f, h * 0.13f)
        // Boyun sağ
        lineTo(cx + w * 0.05f, h * 0.16f)
        // Sağ omuz
        cubicTo(cx + w * 0.12f, h * 0.16f, cx + w * 0.22f, h * 0.17f, cx + w * 0.26f, h * 0.18f)
        // Sağ kol üst
        cubicTo(cx + w * 0.34f, h * 0.2f, cx + w * 0.4f, h * 0.26f, cx + w * 0.42f, h * 0.32f)
        // Sağ dirsek-el
        cubicTo(cx + w * 0.44f, h * 0.36f, cx + w * 0.46f, h * 0.4f, cx + w * 0.45f, h * 0.42f)
        // Sağ el parmaklar
        lineTo(cx + w * 0.47f, h * 0.44f)
        lineTo(cx + w * 0.44f, h * 0.45f)
        lineTo(cx + w * 0.42f, h * 0.43f)
        // Sağ kol iç
        cubicTo(cx + w * 0.38f, h * 0.38f, cx + w * 0.32f, h * 0.3f, cx + w * 0.24f, h * 0.24f)
        // Sağ gövde
        cubicTo(cx + w * 0.22f, h * 0.26f, cx + w * 0.2f, h * 0.32f, cx + w * 0.18f, h * 0.4f)
        // Sağ bel-kalça
        cubicTo(cx + w * 0.16f, h * 0.46f, cx + w * 0.18f, h * 0.5f, cx + w * 0.2f, h * 0.54f)
        // Sağ bacak üst
        cubicTo(cx + w * 0.2f, h * 0.6f, cx + w * 0.18f, h * 0.7f, cx + w * 0.16f, h * 0.78f)
        // Sağ diz-baldır
        cubicTo(cx + w * 0.15f, h * 0.84f, cx + w * 0.14f, h * 0.9f, cx + w * 0.14f, h * 0.94f)
        // Sağ ayak
        lineTo(cx + w * 0.18f, h * 0.96f)
        lineTo(cx + w * 0.18f, h * 0.98f)
        lineTo(cx + w * 0.04f, h * 0.98f)
        lineTo(cx + w * 0.04f, h * 0.96f)
        // Sağ bacak iç
        cubicTo(cx + w * 0.06f, h * 0.88f, cx + w * 0.06f, h * 0.76f, cx + w * 0.06f, h * 0.64f)
        // Kasık
        cubicTo(cx + w * 0.04f, h * 0.58f, cx + w * 0.02f, h * 0.56f, cx, h * 0.55f)
        // Sol bacak iç
        cubicTo(cx - w * 0.02f, h * 0.56f, cx - w * 0.04f, h * 0.58f, cx - w * 0.06f, h * 0.64f)
        cubicTo(cx - w * 0.06f, h * 0.76f, cx - w * 0.06f, h * 0.88f, cx - w * 0.04f, h * 0.96f)
        // Sol ayak
        lineTo(cx - w * 0.04f, h * 0.98f)
        lineTo(cx - w * 0.18f, h * 0.98f)
        lineTo(cx - w * 0.18f, h * 0.96f)
        lineTo(cx - w * 0.14f, h * 0.94f)
        // Sol baldır-diz
        cubicTo(cx - w * 0.14f, h * 0.9f, cx - w * 0.15f, h * 0.84f, cx - w * 0.16f, h * 0.78f)
        // Sol bacak üst
        cubicTo(cx - w * 0.18f, h * 0.7f, cx - w * 0.2f, h * 0.6f, cx - w * 0.2f, h * 0.54f)
        // Sol kalça-bel
        cubicTo(cx - w * 0.18f, h * 0.5f, cx - w * 0.16f, h * 0.46f, cx - w * 0.18f, h * 0.4f)
        // Sol gövde
        cubicTo(cx - w * 0.2f, h * 0.32f, cx - w * 0.22f, h * 0.26f, cx - w * 0.24f, h * 0.24f)
        // Sol kol iç
        cubicTo(cx - w * 0.32f, h * 0.3f, cx - w * 0.38f, h * 0.38f, cx - w * 0.42f, h * 0.43f)
        lineTo(cx - w * 0.44f, h * 0.45f)
        lineTo(cx - w * 0.47f, h * 0.44f)
        // Sol el
        lineTo(cx - w * 0.45f, h * 0.42f)
        cubicTo(cx - w * 0.46f, h * 0.4f, cx - w * 0.44f, h * 0.36f, cx - w * 0.42f, h * 0.32f)
        // Sol kol üst
        cubicTo(cx - w * 0.4f, h * 0.26f, cx - w * 0.34f, h * 0.2f, cx - w * 0.26f, h * 0.18f)
        // Sol omuz
        cubicTo(cx - w * 0.22f, h * 0.17f, cx - w * 0.12f, h * 0.16f, cx - w * 0.05f, h * 0.16f)
        // Boyun sol
        lineTo(cx - w * 0.06f, h * 0.13f)
        // Kafa sol
        cubicTo(cx - w * 0.1f, h * 0.12f, cx - w * 0.12f, h * 0.1f, cx - w * 0.12f, h * 0.07f)
        cubicTo(cx - w * 0.12f, h * 0.04f, cx - w * 0.1f, h * 0.02f, cx, h * 0.02f)
        close()
    }

    // Cam arka plan
    drawPath(silhouette, glassColor)

    // Yağ dolumu
    if (fillLevel > 0f) {
        val totalH = h * 0.98f - h * 0.02f
        val fillTop = h * 0.98f - totalH * fillLevel
        clipPath(silhouette) {
            drawRect(fillColor, Offset(0f, fillTop), Size(w, h - fillTop))
        }
    }

    // Outline
    drawPath(silhouette, outlineColor, style = Stroke(1.5f))
}
