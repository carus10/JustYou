package com.justyou.wellness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justyou.wellness.data.WellnessRepository
import com.justyou.wellness.data.entity.ChallengeStep
import com.justyou.wellness.util.isValidTextInput
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChallengeViewModel(private val repository: WellnessRepository) : ViewModel() {

    val steps: StateFlow<List<ChallengeStep>> = repository.getChallengeSteps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressPercent: StateFlow<Float> = repository.getChallengeSteps()
        .map { list ->
            if (list.isEmpty()) 0f
            else list.count { it.isCompleted }.toFloat() / list.size
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    init {
        viewModelScope.launch { initDefaultSteps() }
    }

    private suspend fun initDefaultSteps() {
        val defaults = listOf(
            "Aynada kendinize gülümseyin",
            "Bir yabancıya merhaba deyin",
            "Bir mağazada satıcıya soru sorun",
            "Bir arkadaşınızı kahveye davet edin",
            "Küçük bir grupta fikrinizi paylaşın",
            "Bir toplantıda söz alın",
            "Yeni bir sosyal etkinliğe katılın",
            "Bir sunum yapın",
            "Tanımadığınız biriyle sohbet başlatın",
            "Büyük bir grup önünde konuşun"
        )
        // Sadece boşsa varsayılanları ekle
        repository.getChallengeSteps().collect { existing ->
            if (existing.isEmpty()) {
                defaults.forEachIndexed { index, desc ->
                    repository.updateChallengeStep(ChallengeStep(index + 1, desc))
                }
            }
            return@collect
        }
    }

    fun toggleStepCompletion(step: ChallengeStep) {
        viewModelScope.launch {
            repository.updateChallengeStep(step.copy(isCompleted = !step.isCompleted))
        }
    }

    fun updateStepDescription(step: ChallengeStep, newDescription: String) {
        if (!isValidTextInput(newDescription)) return
        viewModelScope.launch {
            repository.updateChallengeStep(step.copy(description = newDescription.trim()))
        }
    }
}
