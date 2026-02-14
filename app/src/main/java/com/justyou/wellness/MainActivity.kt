package com.justyou.wellness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.justyou.wellness.data.WellnessRepository
import com.justyou.wellness.ui.navigation.AppNavigation
import com.justyou.wellness.ui.theme.BackgroundPrimary
import com.justyou.wellness.ui.theme.JustYouTheme
import com.justyou.wellness.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as JustYouApplication
        val db = app.database
        val repository = WellnessRepository(
            db.dailyTrackingDao(),
            db.userGoalsDao(),
            db.affirmationDao(),
            db.negativeThoughtDao(),
            db.challengeStepDao()
        )

        val anaSayfaVM = AnaSayfaViewModel(repository, applicationContext)
        val ayarlarVM = AyarlarViewModel(repository)
        val telkinVM = TelkinViewModel(repository)
        val challengeVM = ChallengeViewModel(repository)
        val istatistikVM = IstatistikViewModel(repository)

        setContent {
            JustYouTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundPrimary
                ) {
                    AppNavigation(
                        anaSayfaViewModel = anaSayfaVM,
                        ayarlarViewModel = ayarlarVM,
                        telkinViewModel = telkinVM,
                        challengeViewModel = challengeVM,
                        istatistikViewModel = istatistikVM
                    )
                }
            }
        }
    }
}
