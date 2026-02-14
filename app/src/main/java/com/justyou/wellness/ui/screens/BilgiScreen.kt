package com.justyou.wellness.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.justyou.wellness.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BilgiScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Uygulama Hakkında", color = TextPrimary) },
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
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardBackground)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("JustYOU", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "JustYOU, sosyal kaygı yönetimi ve kişisel gelişim odaklı bir wellness uygulamasıdır. " +
                        "Günlük su, kalori ve adım takibi yapabilir, olumlu düşünce telkinleri oluşturabilir, " +
                        "olumsuz otomatik düşüncelerinizi yönetebilir ve 10 adımlı korku hiyerarşisi challenge'ı ile " +
                        "kendinizi geliştirebilirsiniz.",
                        fontSize = 14.sp, color = TextSecondary, lineHeight = 22.sp
                    )
                }
            }
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardBackground)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Kullanım Kılavuzu", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Ana Sayfa: Su, kalori ve adım takibinizi yapın\n" +
                        "• Ayarlar: Günlük hedeflerinizi belirleyin, telkin ve OOD listenizi yönetin\n" +
                        "• Telkin: Olumlu düşünce kartlarınızı görüntüleyin\n" +
                        "• Eğitim: Sosyal kaygı hakkında bilgi edinin\n" +
                        "• Challenge: 10 adımlı korku hiyerarşisini tamamlayın\n" +
                        "• İstatistik: Haftalık ilerlemenizi takip edin",
                        fontSize = 14.sp, color = TextSecondary, lineHeight = 22.sp
                    )
                }
            }
        }
    }
}
