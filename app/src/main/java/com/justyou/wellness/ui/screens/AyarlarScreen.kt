package com.justyou.wellness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.ui.theme.*
import com.justyou.wellness.viewmodel.AyarlarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyarlarScreen(viewModel: AyarlarViewModel, onBack: () -> Unit = {}) {
    val goals by viewModel.goals.collectAsState()
    val affirmations by viewModel.affirmations.collectAsState()
    val negativeThoughts by viewModel.negativeThoughts.collectAsState()

    var showTelkinDialog by remember { mutableStateOf(false) }
    var showYeniSenDialog by remember { mutableStateOf(false) }
    var newTelkinText by remember { mutableStateOf("") }
    var showTelkinError by remember { mutableStateOf(false) }

    // Yeni Sen dialog state
    var yeniSenText1 by remember { mutableStateOf("") }
    var yeniSenText2 by remember { mutableStateOf("") }
    var showYeniSenError by remember { mutableStateOf(false) }

    // Hedef düzenleme dialog
    var showGoalDialog by remember { mutableStateOf(false) }
    var editingGoal by remember { mutableStateOf("") }
    var goalInput by remember { mutableStateOf("") }

    var selectedList by remember { mutableStateOf("Telkin Listesi") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Hedef düzenleme dialog
    if (showGoalDialog) {
        val title = when (editingGoal) {
            "su" -> "Su Hedefi (ml)"
            "kalori" -> "Kalori Hedefi (kcal)"
            else -> "Adım Hedefi"
        }
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = { Text(title) },
            text = {
                OutlinedTextField(
                    value = goalInput,
                    onValueChange = { goalInput = it.filter { c -> c.isDigit() } },
                    label = { Text("Değer girin") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    goalInput.toIntOrNull()?.let { v ->
                        if (v > 0) {
                            when (editingGoal) {
                                "su" -> viewModel.updateGoals(goals.copy(waterGoal = v))
                                "kalori" -> viewModel.updateGoals(goals.copy(calorieGoal = v))
                                "adim" -> viewModel.updateGoals(goals.copy(stepGoal = v))
                            }
                        }
                    }
                    showGoalDialog = false
                }) { Text("Kaydet") }
            },
            dismissButton = {
                TextButton(onClick = { showGoalDialog = false }) { Text("İptal") }
            }
        )
    }

    // Günlük Telkin Ekle dialog
    if (showTelkinDialog) {
        AlertDialog(
            onDismissRequest = { showTelkinDialog = false; newTelkinText = ""; showTelkinError = false },
            title = { Text("Günlük Telkin Ekle") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTelkinText,
                        onValueChange = { newTelkinText = it; showTelkinError = false },
                        label = { Text("Telkin metni girin") },
                        isError = showTelkinError, singleLine = false
                    )
                    if (showTelkinError) Text("Boş metin eklenemez", color = NegativeRed, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val success = viewModel.addAffirmation(newTelkinText)
                    if (success) { newTelkinText = ""; showTelkinDialog = false; showTelkinError = false }
                    else showTelkinError = true
                }) { Text("Ekle") }
            },
            dismissButton = {
                TextButton(onClick = { showTelkinDialog = false; newTelkinText = ""; showTelkinError = false }) { Text("İptal") }
            }
        )
    }

    // Yeni Sen dialog - 2 metin alanı
    if (showYeniSenDialog) {
        AlertDialog(
            onDismissRequest = { showYeniSenDialog = false; yeniSenText1 = ""; yeniSenText2 = ""; showYeniSenError = false },
            title = { Text("Yeni Sen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Sizi özgüvenden alıkoyan durum veya duygu giriniz",
                        fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary
                    )
                    OutlinedTextField(
                        value = yeniSenText1,
                        onValueChange = { yeniSenText1 = it; showYeniSenError = false },
                        placeholder = { Text("Metin giriniz") },
                        isError = showYeniSenError && yeniSenText1.isBlank(),
                        singleLine = false, minLines = 2
                    )
                    Text(
                        "Bu alıkoyma aslında gerçek değil, kendiniz hakkında düşünün ve bu hisse/düşünceye yanıt verin",
                        fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary
                    )
                    OutlinedTextField(
                        value = yeniSenText2,
                        onValueChange = { yeniSenText2 = it; showYeniSenError = false },
                        placeholder = { Text("Metin giriniz") },
                        isError = showYeniSenError && yeniSenText2.isBlank(),
                        singleLine = false, minLines = 2
                    )
                    if (showYeniSenError) Text("Her iki alan da doldurulmalıdır", color = NegativeRed, fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (yeniSenText1.isBlank() || yeniSenText2.isBlank()) {
                        showYeniSenError = true
                    } else {
                        viewModel.addNegativeThought(yeniSenText1.trim(), yeniSenText2.trim())
                        yeniSenText1 = ""; yeniSenText2 = ""; showYeniSenDialog = false; showYeniSenError = false
                    }
                }) { Text("Tamam") }
            },
            dismissButton = {
                TextButton(onClick = { showYeniSenDialog = false; yeniSenText1 = ""; yeniSenText2 = ""; showYeniSenError = false }) { Text("İptal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ayarlar", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Hedef belirleme", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GoalChip("Su : ${String.format("%.1f", goals.waterGoal / 1000f)} L", Modifier.weight(1f)) {
                        editingGoal = "su"; goalInput = goals.waterGoal.toString(); showGoalDialog = true
                    }
                    GoalChip("kcal : ${goals.calorieGoal}", Modifier.weight(1f)) {
                        editingGoal = "kalori"; goalInput = goals.calorieGoal.toString(); showGoalDialog = true
                    }
                    GoalChip("Adım : ${goals.stepGoal}", Modifier.weight(1f)) {
                        editingGoal = "adim"; goalInput = goals.stepGoal.toString(); showGoalDialog = true
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                GradientButton("Günlük Telkin Ekle", listOf(Color(0xFFB8E0D2), Color(0xFFC7E9F1))) {
                    showTelkinDialog = true
                }
            }
            item {
                GradientButton("Yeni Sen", listOf(Color(0xFFD8B4F8), Color(0xFFF5C6D6))) {
                    showYeniSenDialog = true
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Liste", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    ExposedDropdownMenuBox(expanded = dropdownExpanded, onExpandedChange = { dropdownExpanded = !dropdownExpanded }) {
                        FilterChip(
                            selected = true, onClick = { dropdownExpanded = true },
                            label = { Text(selectedList, fontSize = 12.sp) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
                            DropdownMenuItem(text = { Text("Telkin Listesi") }, onClick = { selectedList = "Telkin Listesi"; dropdownExpanded = false })
                            DropdownMenuItem(text = { Text("Yeni Sen Listesi") }, onClick = { selectedList = "Yeni Sen Listesi"; dropdownExpanded = false })
                        }
                    }
                }
            }

            if (selectedList == "Telkin Listesi") {
                items(affirmations, key = { it.id }) { item ->
                    ListItemRow(item.text) { viewModel.deleteAffirmation(item) }
                }
            } else {
                items(negativeThoughts, key = { it.id }) { item ->
                    YeniSenListItem(item.text, item.response) { viewModel.deleteNegativeThought(item) }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun GoalChip(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(label, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun GradientButton(text: String, colors: List<Color>, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(colors), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) { Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary) }
    }
}

@Composable
private fun ListItemRow(text: String, onDelete: () -> Unit) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardBackground)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text, modifier = Modifier.weight(1f), fontSize = 14.sp, color = TextPrimary)
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.RemoveCircle, "Sil", tint = NegativeRed)
            }
        }
    }
}

@Composable
private fun YeniSenListItem(thought: String, response: String, onDelete: () -> Unit) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardBackground)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text("😟 $thought", fontSize = 13.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text("😊 $response", fontSize = 13.sp, color = PositiveGreen)
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.RemoveCircle, "Sil", tint = NegativeRed)
            }
        }
    }
}
