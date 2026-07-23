package org.muslim_voice.project.features.splash.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.data.UserPreferencesRepository
import org.muslim_voice.project.features.splash.effect.SplashEffect
import org.muslim_voice.project.features.splash.state.SplashState

class SplashViewModel(
    private val preferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SplashEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            val steps = 28
            repeat(steps) { step ->
                delay(50L)
                _state.update { it.copy(progress = (step + 1) / steps.toFloat()) }
            }
            _state.update { it.copy(isReady = true, progress = 1f) }
            delay(200L)
            val onboardingDone = preferencesRepository.isOnboardingDone()
            _effect.emit(
                if (onboardingDone) SplashEffect.NavigateToMain else SplashEffect.NavigateToOnboarding,
            )
        }
    }
}
