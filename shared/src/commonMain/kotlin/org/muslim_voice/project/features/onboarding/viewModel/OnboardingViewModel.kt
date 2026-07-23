package org.muslim_voice.project.features.onboarding.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.data.UserPreferencesRepository
import org.muslim_voice.project.features.onboarding.effect.OnboardingEffect
import org.muslim_voice.project.features.onboarding.intent.OnboardingIntent
import org.muslim_voice.project.features.onboarding.state.OnboardingState

class OnboardingViewModel(
    private val preferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OnboardingEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<OnboardingEffect> = _effect.asSharedFlow()

    fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.Back -> _state.update {
                it.copy(currentPage = (it.currentPage - 1).coerceAtLeast(0))
            }
            OnboardingIntent.Next -> {
                val lastIndex = _state.value.pages.lastIndex
                if (_state.value.currentPage >= lastIndex) {
                    finishOnboarding()
                } else {
                    _state.update { it.copy(currentPage = it.currentPage + 1) }
                }
            }
            OnboardingIntent.Skip, OnboardingIntent.Finish -> finishOnboarding()
            is OnboardingIntent.SetPage -> _state.update {
                it.copy(currentPage = intent.page.coerceIn(0, it.pages.lastIndex))
            }
        }
    }

    private fun finishOnboarding() {
        viewModelScope.launch {
            preferencesRepository.setOnboardingDone(true)
            _effect.emit(OnboardingEffect.NavigateToLogin)
        }
    }
}
