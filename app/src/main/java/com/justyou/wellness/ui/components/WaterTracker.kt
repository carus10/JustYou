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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.ui.theme.*
import com.justyou.wellness.util.calculateWaterLevel

@Composable
fun WaterTracker(
    currentWater: Int,
    goalWater: Int,
    onAddWater: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val level by animateFloatAsState(
        targetValue = calculateWaterLevel(currentWater, goalWater),
        animationSpec = tween(600), label = "waterLevel"
    )
    val displayL = String.format("%.1f", currentWater / 1000f)
    val goalL = String.format("%.1f", goalWater / 1000f)

    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Su Ekle") },
            text = {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it.filter { c -> c.isDigit() } },
                    label = { Text("Miktar (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    inputText.toIntOrNull()?.let { if (it > 0) onAddWater(it) }
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
            Text("Su Takip", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Canvas(modifier = Modifier.size(70.dp, 100.dp)) {
                val w = size.width; val h = size.height
                val bottleW = w * 0.55f; val bL = (w - bottleW) / 2
                val neckW = bottleW * 0.35f; val nL = (w - neckW) / 2
                val capH = h * 0.06f; val neckTop = capH; val neckBot = h * 0.22f; val bodyBot = h * 0.95f
                drawRoundRect(Color(0xFFB0B8C0), Offset(nL - 2, 0f), Size(neckW + 4, capH), CornerRadius(4f))
                drawRect(Color(0xFFD0E4F0), Offset(nL, neckTop), Size(neckW, neckBot - neckTop))
                drawRect(Color(0xFF90B8D0), Offset(nL, neckTop), Size(neckW, neckBot - neckTop), style = Stroke(2f))
                drawRoundRect(Color(0xFFD0E4F0), Offset(bL, neckBot), Size(bottleW, bodyBot - neckBot), CornerRadius(10f))
                drawRoundRect(Color(0xFF90B8D0), Offset(bL, neckBot), Size(bottleW, bodyBot - neckBot), CornerRadius(10f), style = Stroke(2f))
                val waterH = (bodyBot - neckBot - 4) * level
                if (waterH > 0) {
                    drawRoundRect(Color(0xFF7EC8E3), Offset(bL + 2, bodyBot - waterH - 2), Size(bottleW - 4, waterH), CornerRadius(8f))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Bugün İçilen: $displayL L / Hedef: $goalL L", fontSize = 10.sp, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonGradientStart),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) { Text("+ Su Ekle", fontSize = 12.sp, color = TextPrimary) }
        }
    }
}
