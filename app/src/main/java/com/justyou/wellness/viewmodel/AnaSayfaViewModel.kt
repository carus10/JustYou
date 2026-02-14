package com.justyou.wellness.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justyou.wellness.data.WellnessRepository
import com.justyou.wellness.data.entity.DailyTracking
import com.justyou.wellness.data.entity.UserGoals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class AnaSayfaViewModel(
    private val repository: WellnessRepository,
    private val context: Context
) : ViewModel() {

    val todayTracking: StateFlow<DailyTracking> = repository.getTodayTracking()
        .map { it ?: DailyTracking(date = "") }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DailyTracking(date = ""))

    val goals: StateFlow<UserGoals> = repository.getGoals()
        .map { it ?: UserGoals() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserGoals())

    private val _dailyAffirmation = MutableStateFlow<String?>(null)
    val dailyAffirmation: StateFlow<String?> = _dailyAffirmation.asStateFlow()

    private val prefs = context.getSharedPreferences("daily_telkin", Context.MODE_PRIVATE)

    init {
        loadDailyAffirmation()
    }

    fun loadDailyAffirmation() {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val savedDate = prefs.getString("telkin_date", null)
            val savedText = prefs.getString("telkin_text", null)

            if (savedDate == today && savedText != null) {
                // Bugün zaten seçilmiş, aynısını göster
                _dailyAffirmation.value = savedText
            } else {
                // Yeni gün, rastgele seç
                val text = repository.getRandomAffirmationText()
                _dailyAffirmation.value = text
                if (text != null) {
                    prefs.edit().putString("telkin_date", today).putString("telkin_text", text).apply()
                }
            }
        }
    }

    fun addWater(amount: Int) {
        if (amount > 0) viewModelScope.launch { repository.addWater(amount) }
    }

    fun addCalories(amount: Int) {
        if (amount > 0) viewModelScope.launch { repository.addCalories(amount) }
    }

    fun addSteps(amount: Int) {
        if (amount > 0) viewModelScope.launch { repository.addSteps(amount) }
    }
}
