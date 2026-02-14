package com.justyou.wellness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justyou.wellness.data.WellnessRepository
import com.justyou.wellness.data.entity.Affirmation
import com.justyou.wellness.data.entity.NegativeThought
import com.justyou.wellness.data.entity.UserGoals
import com.justyou.wellness.util.isValidTextInput
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AyarlarViewModel(private val repository: WellnessRepository) : ViewModel() {

    val goals: StateFlow<UserGoals> = repository.getGoals()
        .map { it ?: UserGoals() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserGoals())

    val affirmations: StateFlow<List<Affirmation>> = repository.getAllAffirmations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val negativeThoughts: StateFlow<List<NegativeThought>> = repository.getAllNegativeThoughts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateGoals(goals: UserGoals) {
        viewModelScope.launch { repository.updateGoals(goals) }
    }

    fun addAffirmation(text: String): Boolean {
        if (!isValidTextInput(text)) return false
        viewModelScope.launch { repository.addAffirmation(text.trim()) }
        return true
    }

    fun deleteAffirmation(affirmation: Affirmation) {
        viewModelScope.launch { repository.deleteAffirmation(affirmation) }
    }

    fun addNegativeThought(text: String, response: String): Boolean {
        if (!isValidTextInput(text) || !isValidTextInput(response)) return false
        viewModelScope.launch { repository.addNegativeThought(text.trim(), response.trim()) }
        return true
    }

    fun deleteNegativeThought(thought: NegativeThought) {
        viewModelScope.launch { repository.deleteNegativeThought(thought) }
    }
}
