package com.justyou.wellness.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.ui.components.CalorieTracker
import com.justyou.wellness.ui.components.StepTracker
import com.justyou.wellness.ui.components.WaterTracker
import com.justyou.wellness.ui.theme.*
import com.justyou.wellness.viewmodel.AnaSayfaViewModel
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnaSayfaScreen(
    viewModel: AnaSayfaViewModel,
    onInfoClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val tracking by viewModel.todayTracking.collectAsState()
    val goals by viewModel.goals.collectAsState()
    val dailyAffirmation by viewModel.dailyAffirmation.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ana Sayfa", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onInfoClick) {
                        Icon(Icons.Default.Info, contentDescription = "Bilgi", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Ayarlar", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundPrimary)
            )
        },
        containerColor = BackgroundPrimary
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Günlük Telkinler motivasyon kartı
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Günlük Telkinler", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (dailyAffirmation != null) {
                        Text(
                            dailyAffirmation!!,
                            fontSize = 14.sp, color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            "Telkinleriniz",
                            fontSize = 14.sp, color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Su Takip ve Kalori Takip yan yana - aynı yükseklik
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WaterTracker(
                    currentWater = tracking.waterIntake,
                    goalWater = goals.waterGoal,
                    onAddWater = { viewModel.addWater(it) },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                CalorieTracker(
                    currentCalories = tracking.calorieIntake,
                    goalCalories = goals.calorieGoal,
                    onAddCalories = { viewModel.addCalories(it) },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            StepTracker(
                currentSteps = tracking.stepCount,
                goalSteps = goals.stepGoal,
                onAddSteps = { viewModel.addSteps(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
