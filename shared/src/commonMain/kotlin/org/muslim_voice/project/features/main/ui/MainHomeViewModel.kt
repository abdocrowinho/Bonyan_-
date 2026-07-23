package org.muslim_voice.project.features.mainHome.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.data.helper.AppError
import org.muslim_voice.project.core.data.helper.RequestState
import org.muslim_voice.project.core.domain.mapper.PrayerTimesUiMapper
import org.muslim_voice.project.core.domain.usecase.GetPrayerTimesUseCase
import org.muslim_voice.project.features.mainHome.effect.MainHomeUiEffect
import org.muslim_voice.project.features.main.ui.MainHomeIntent
import org.muslim_voice.project.features.mainHome.state.MainHomeUiState

class MainHomeViewModel(
    private val getPrayerTimesUseCase: GetPrayerTimesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainHomeUiState())
    val uiState: StateFlow<MainHomeUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<MainHomeUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private var loadJob: Job? = null
    private var lastCity: String = MainHomeIntent.DEFAULT_CITY
    private var lastCountry: String = MainHomeIntent.DEFAULT_COUNTRY

    init {
        onIntent(
            MainHomeIntent.LoadPrayerTimes(
                city = MainHomeIntent.DEFAULT_CITY,
                country = MainHomeIntent.DEFAULT_COUNTRY,
            ),
        )
    }

    fun onIntent(intent: MainHomeIntent) {
        when (intent) {
            is MainHomeIntent.LoadPrayerTimes -> loadPrayerTimes(intent.city, intent.country)
            is MainHomeIntent.RefreshPrayerTimes -> loadPrayerTimes(intent.city, intent.country)
            is MainHomeIntent.NavigateToQibla -> emitEffect(MainHomeUiEffect.NavigateToQibla)
            is MainHomeIntent.SelectTab -> handleTabSelection(intent.index)
        }
    }

    private fun loadPrayerTimes(city: String, country: String) {
        lastCity = city
        lastCountry = country
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getPrayerTimesUseCase(city = city, country = country).collect { state ->
                when (state) {
                    is RequestState.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = state.isLoading,
                                appError = null,
                            )
                        }
                    }
                    is RequestState.Success -> {
                        val uiData = PrayerTimesUiMapper.map(state.data)
                        _uiState.update {
                            it.copy(
                                prayerTimes = state.data,
                                prayerTimeItems = uiData.items,
                                currentPrayerName = uiData.currentPrayerName,
                                currentTime = uiData.currentTime,
                                remainingTime = uiData.remainingTime,
                                hijriDateLabel = uiData.hijriDateLabel,
                                gregorianDateLabel = uiData.gregorianDateLabel,
                                isLoading = false,
                                isOffline = state.isFromCache,
                                appError = null,
                            )
                        }
                    }
                    is RequestState.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                appError = state.error,
                                isOffline = state.error == AppError.Connection,
                            )
                        }
                        if (state.error != AppError.Connection) {
                            emitEffect(MainHomeUiEffect.ShowError(resolveErrorMessage(state.error)))
                        }
                    }
                }
            }
        }
    }

    private fun handleTabSelection(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
        val effect = when (index) {
            1 -> MainHomeUiEffect.NavigateToQuran
            2 -> MainHomeUiEffect.NavigateToGroups
            3 -> MainHomeUiEffect.NavigateToProfile
            else -> null
        }
        effect?.let { emitEffect(it) }
    }

    private fun resolveErrorMessage(error: AppError): String = when (error) {
        AppError.Connection -> "لا يوجد اتصال بالإنترنت"
        AppError.Unknown -> "حدث خطأ غير متوقع"
        is AppError.Server -> error.message
        is AppError.Validation -> error.message
    }

    private fun emitEffect(effect: MainHomeUiEffect) {
        viewModelScope.launch { _uiEffect.send(effect) }
    }
}
