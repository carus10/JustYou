package com.justyou.wellness.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun EgitimScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eğitim", color = TextPrimary) },
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionCard(
                    "🌱 Sosyal Anksiyete Bir Kader Değil",
                    "Sosyal anksiyete, basit bir çekingenlik ya da karakter özelliği değildir. " +
                    "Zihnin belirli savunma mekanizmalarını yanlış zamanda ve yanlış şiddette devreye soktuğu " +
                    "klinik bir tablodur. Bu mekanizmayı anlamak, ipleri beyninizin elinden alıp kendi elinize " +
                    "almanız demektir.\n\n" +
                    "Bilişsel Davranışçı Terapi (BDT) ekolünün en güncel tekniklerinden süzülmüş bu rehber, " +
                    "sizi anksiyetenin avı olmaktan çıkarıp kendi zihinsel süreçlerinizin aktif yöneticisi " +
                    "haline getirmeyi amaçlar."
                )
            }

            item {
                SectionCard(
                    "🧬 Genetik ve Çevresel Temeller",
                    "Sosyal anksiyetenin oluşumu, genetik yatkınlık ile çevresel travmaların birleşimiyle şekillenir. " +
                    "Sadece genetik yatkınlık genellikle tam kapasiteli bir sosyal anksiyete yaratmaya yetmez; " +
                    "üzerine çevresel bir tetikleyici gerekir.\n\n" +
                    "Ailenizde anksiyete, OKB veya depresyon geçmişi varsa beyniniz kaygı sinyallerine daha " +
                    "duyarlı olabilir. Ancak bu biyolojik bir başlangıç noktasıdır, bir son değildir.\n\n" +
                    "Çocukluk veya ergenlikte yaşanan ağır reddedilme, zorbalık ya da yoğun utanç deneyimleri " +
                    "beyne şunu öğretir: \"Sosyal etkileşim hayati bir tehlikedir.\" Hissettiğiniz terleme, " +
                    "titreme ve ses kısılması, beyninizin sizi hayali bir tehlikeden koruma çabasıdır."
                )
            }

            item {
                SectionCard(
                    "🧠 Temel İnançlar ve Otomatik Düşünceler",
                    "Davranışlarımızı yöneten asıl güç, zihnimizin derinliklerine yerleşmiş bilişsel şemalardır.\n\n" +
                    "Temel İnançlar: Kendinizle ilgili en derindeki kemikleşmiş yargılarınızdır. " +
                    "\"Değersizim\", \"Sıkıcıyım\", \"Kimse beni sevemez\" gibi.\n\n" +
                    "Olumsuz Otomatik Düşünceler: Sosyal bir durumda anlık olarak zihninizde çakan düşüncelerdir. " +
                    "Siz sadece kaygıyı hissedersiniz ama arkada \"Beni rezil edecekler\" gibi düşünceler " +
                    "çoktan aksiyon almıştır.\n\n" +
                    "Seçici Algı: 9 tane olumlu etkileşim yaşasanız bile, zihniniz o tek olumsuz olayı " +
                    "cımbızla çeker ve \"İşte ben demiştim, ben yetersizim\" diyerek hatalı inancınızı besler."
                )
            }

            item {
                SectionCard(
                    "🔄 Kognitif Yeniden Çerçeveleme",
                    "Mantıksız inançları sorgulama ve rasyonel bir süzgeçten geçirme işlemidir. " +
                    "Burada \"pozitif düşünce\" ile değil, \"objektif kanıtlar\" ile ilerleriz.\n\n" +
                    "Örneğin birisi size kaba davranıyorsa, bu sizin sevilmezliğinizin kanıtı değildir. " +
                    "Birinin durduk yere zorbaca davranması, o kişinin duygusal zekasının düşük olduğunun " +
                    "göstergesidir; sizin değerinizin değil.\n\n" +
                    "Kendinize sorun:\n" +
                    "• Sence ben temelde nasıl biriyim?\n" +
                    "• İnsanlara yaklaştığımda tam olarak ne olmasını bekliyorum?\n\n" +
                    "Bu soruların cevaplarını yazıya dökmek, olumsuz şemalarınızı görünür kılmanın ilk adımıdır. " +
                    "Ayarlar bölümündeki \"Yeni Sen\" özelliğini kullanarak olumsuz düşüncelerinize " +
                    "rasyonel yanıtlar oluşturabilirsiniz."
                )
            }

            item {
                SectionCard(
                    "⛰️ Kademeli Maruz Bırakma: Korku Hiyerarşisi",
                    "Teorik bilgi, tecrübe ile mühürlenmediği sürece beyinde yeni bir nöron yolu oluşturamaz. " +
                    "Rasyonel düşüncelerinizin doğruluğunu beyninize kanıtlamak için sahaya inmelisiniz.\n\n" +
                    "Asla en yüksek şiddetteki görevle başlamayın. Direkt en büyük korkunuzun üzerine giderseniz " +
                    "başarısız olur ve olumsuz inancınızı daha da kemikleştirirsiniz.\n\n" +
                    "Düşük şiddet (3/10): Yoldan geçen birine saati sormak. Amacınız dünyanın başınıza " +
                    "yıkılmadığını görmek.\n\n" +
                    "Orta şiddet (5/10): Birinin kıyafetini nereden aldığını sormak veya kısa bir selamlaşma " +
                    "başlatmak.\n\n" +
                    "Yüksek şiddet (7+/10): Hoşlandığınız birinden Instagram hesabını istemek veya bir " +
                    "topluluğa hitap etmek.\n\n" +
                    "Bir seviyeyi fethetmeden diğerine geçmeyin. Challenge bölümünden kendi korku " +
                    "hiyerarşinizi oluşturabilir ve adımlarınızı kendinize göre düzenleyebilirsiniz."
                )
            }

            item {
                SectionCard(
                    "🦋 Yeni Bir Sosyal Kimlik",
                    "Sosyal anksiyeteden özgürleşmek sabır isteyen, stratejik bir süreçtir. " +
                    "20-30 yıldır zihninizde olan şemaları değiştirmek için kendinize zaman tanıyın.\n\n" +
                    "Siz artık otomatik tepkilerin mahkumu değil, bu mekanizmanın yöneticisisiniz. " +
                    "Bu rehberdeki teknikleri hayatınıza entegre ettiğinizde, anksiyetenin hayatınızı " +
                    "felç etmesine izin vermediğinizi ve korkuya rağmen hareket edebilme becerisi " +
                    "kazandığınızı göreceksiniz.\n\n" +
                    "Sabırla devam edin; fethettiğiniz her seviye, özgürlüğünüze giden yolda bir tuğladır."
                )
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            Text(content, fontSize = 14.sp, color = TextSecondary, lineHeight = 22.sp)
        }
    }
}
