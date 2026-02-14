package com.justyou.wellness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.data.entity.ChallengeStep
import com.justyou.wellness.ui.theme.*
import com.justyou.wellness.viewmodel.ChallengeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(viewModel: ChallengeViewModel, onBack: () -> Unit) {
    val steps by viewModel.steps.collectAsState()
    val progress by viewModel.progressPercent.collectAsState()

    var editingStep by remember { mutableStateOf<ChallengeStep?>(null) }
    var editText by remember { mutableStateOf("") }

    // Düzenleme dialog
    if (editingStep != null) {
        AlertDialog(
            onDismissRequest = { editingStep = null },
            title = { Text("Adım ${editingStep!!.stepNumber} Düzenle") },
            text = {
                OutlinedTextField(
                    value = editText,
                    onValueChange = { editText = it },
                    label = { Text("Adım açıklaması") },
                    singleLine = false, minLines = 2
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (editText.isNotBlank()) {
                        viewModel.updateStepDescription(editingStep!!, editText)
                        editingStep = null
                    }
                }) { Text("Kaydet") }
            },
            dismissButton = {
                TextButton(onClick = { editingStep = null }) { Text("İptal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Challenge", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundPrimary)
            )
        },
        containerColor = BackgroundPrimary
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // İlerleme çubuğu
            Column(modifier = Modifier.padding(16.dp)) {
                Text("İlerleme: ${(progress * 100).toInt()}%", fontSize = 14.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ProgressBackground)
                ) {
                    Box(
                        modifier = Modifier.fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.horizontalGradient(listOf(ProgressFillStart, ProgressFillEnd)))
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(steps, key = { it.stepNumber }) { step ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = step.isCompleted,
                                onCheckedChange = { viewModel.toggleStepCompletion(step) },
                                colors = CheckboxDefaults.colors(checkedColor = PastelBlue)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Adım ${step.stepNumber}",
                                    fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.Bold
                                )
                                Text(
                                    step.description,
                                    fontSize = 14.sp,
                                    color = if (step.isCompleted) TextMuted else TextPrimary,
                                    textDecoration = if (step.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                )
                            }
                            IconButton(
                                onClick = { editingStep = step; editText = step.description },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Edit, "Düzenle", tint = TextMuted, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
