package com.justyou.wellness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justyou.wellness.data.WellnessRepository
import com.justyou.wellness.data.entity.Affirmation
import com.justyou.wellness.data.entity.NegativeThought
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TelkinViewModel(private val repository: WellnessRepository) : ViewModel() {

    val affirmations: StateFlow<List<Affirmation>> = repository.getAllAffirmations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val negativeThoughts: StateFlow<List<NegativeThought>> = repository.getAllNegativeThoughts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Rastgele seçilen "Yeni Sen" çifti (her sayfaya girildiğinde yenilenir)
    private val _randomThought = MutableStateFlow<NegativeThought?>(null)
    val randomThought: StateFlow<NegativeThought?> = _randomThought.asStateFlow()

    fun loadRandomThought() {
        viewModelScope.launch {
            _randomThought.value = repository.getRandomNegativeThought()
        }
    }
}
