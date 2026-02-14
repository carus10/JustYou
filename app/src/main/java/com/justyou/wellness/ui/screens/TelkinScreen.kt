package com.justyou.wellness.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.ui.theme.*
import com.justyou.wellness.viewmodel.TelkinViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelkinScreen(
    viewModel: TelkinViewModel,
    onSettingsClick: () -> Unit,
    onEgitimClick: () -> Unit,
    onChallengeClick: () -> Unit
) {
    val randomThought by viewModel.randomThought.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val oodCardColor = Color(0xFFF5C6D6)
    val telkinCardColor = Color(0xFFB8E0D2)
    val oodIconColor = Color(0xFFD4728C)
    val telkinIconColor = Color(0xFF7BAF9E)

    // Her sayfaya girildiğinde rastgele "Yeni Sen" çifti yükle
    LaunchedEffect(Unit) {
        viewModel.loadRandomThought()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Menü", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(label = { Text("Eğitim") }, selected = false, onClick = { scope.launch { drawerState.close() }; onEgitimClick() })
                NavigationDrawerItem(label = { Text("Challenge") }, selected = false, onClick = { scope.launch { drawerState.close() }; onChallengeClick() })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Telkin Benimseme", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menü", tint = TextPrimary)
                        }
                    },
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(Icons.Default.Settings, "Ayarlar", tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundPrimary)
                )
            },
            containerColor = BackgroundPrimary
        ) { padding ->
            if (randomThought == null) {
                // Hiç "Yeni Sen" eklenmemişse boş ekran
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {}
            } else {
                val thought = randomThought!!
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Kırmızı surat - olumsuz düşünce (1. metin)
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = oodCardColor),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.SentimentDissatisfied, null, tint = oodIconColor, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(thought.text, fontSize = 14.sp, color = TextPrimary)
                        }
                    }

                    // Yeşil surat - olumlu yanıt (2. metin)
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = telkinCardColor),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.SentimentSatisfied, null, tint = telkinIconColor, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(thought.response, fontSize = 14.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}
