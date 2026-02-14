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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StepTracker(
    currentSteps: Int,
    goalSteps: Int,
    onAddSteps: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (goalSteps > 0) (currentSteps.toFloat() / goalSteps).coerceIn(0f, 1f) else 0f,
        animationSpec = tween(600), label = "stepProgress"
    )
    val fmt = NumberFormat.getNumberInstance(Locale("tr"))

    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Adım Ekle") },
            text = {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it.filter { c -> c.isDigit() } },
                    label = { Text("Adım sayısı") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    inputText.toIntOrNull()?.let { if (it > 0) onAddSteps(it) }
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
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Günlük Adım Takip", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))

            // Koşan adam + segmentli progress bar
            Canvas(modifier = Modifier.fillMaxWidth().height(70.dp)) {
                val w = size.width
                val h = size.height
                val totalSegments = 20
                val filledSegments = (totalSegments * progress).toInt()

                val activeColor = Color(0xFF4CAF50) // yeşil
                val inactiveColor = Color(0xFF9E9E9E) // gri
                val runnerColor = Color(0xFF7B1FA2) // mor

                // Koşan adam figürü - progress pozisyonunda
                val runnerX = w * 0.1f + (w * 0.8f) * progress
                val runnerY = h * 0.01f
                drawRunningMan(runnerX, runnerY, runnerColor)

                // Segmentli bar
                val barY = h * 0.55f
                val barH = h * 0.35f
                val barPadding = 8f
                val segGap = 4f
                val totalBarW = w - barPadding * 2
                val segW = (totalBarW - segGap * (totalSegments - 1)) / totalSegments

                // Bar arka plan (çerçeve)
                drawRoundRect(
                    Color(0xFFE0E0E0),
                    Offset(barPadding - 4, barY - 4),
                    Size(totalBarW + 8, barH + 8),
                    CornerRadius(barH / 2 + 4)
                )

                for (i in 0 until totalSegments) {
                    val x = barPadding + i * (segW + segGap)
                    val segColor = if (i < filledSegments) activeColor else inactiveColor
                    drawRoundRect(segColor, Offset(x, barY), Size(segW, barH), CornerRadius(4f))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("${fmt.format(currentSteps)} / ${fmt.format(goalSteps)} Adım", fontSize = 14.sp, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonGradientStart),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) { Text("+ Adım Ekle", fontSize = 13.sp, color = TextPrimary) }
        }
    }
}

private fun DrawScope.drawRunningMan(cx: Float, topY: Float, color: Color) {
    val s = 1.8f // scale - büyük

    // Kafa
    drawCircle(color, 5f * s, Offset(cx, topY + 5f * s))

    // Gövde - öne eğik
    val bodyPath = Path().apply {
        moveTo(cx - 1f * s, topY + 10f * s)
        lineTo(cx - 4f * s, topY + 24f * s)
    }
    drawPath(bodyPath, color, style = Stroke(2.5f * s))

    // Sol kol (arkada)
    val leftArm = Path().apply {
        moveTo(cx - 2f * s, topY + 13f * s)
        lineTo(cx + 4f * s, topY + 18f * s)
    }
    drawPath(leftArm, color, style = Stroke(2f * s))

    // Sağ kol (önde)
    val rightArm = Path().apply {
        moveTo(cx - 2f * s, topY + 13f * s)
        lineTo(cx - 8f * s, topY + 17f * s)
    }
    drawPath(rightArm, color, style = Stroke(2f * s))

    // Sol bacak (arkada, gergin)
    val leftLeg = Path().apply {
        moveTo(cx - 4f * s, topY + 24f * s)
        lineTo(cx + 3f * s, topY + 32f * s)
        lineTo(cx + 6f * s, topY + 34f * s)
    }
    drawPath(leftLeg, color, style = Stroke(2f * s))

    // Sağ bacak (önde, bükük)
    val rightLeg = Path().apply {
        moveTo(cx - 4f * s, topY + 24f * s)
        lineTo(cx - 8f * s, topY + 30f * s)
        lineTo(cx - 6f * s, topY + 34f * s)
    }
    drawPath(rightLeg, color, style = Stroke(2f * s))
}
