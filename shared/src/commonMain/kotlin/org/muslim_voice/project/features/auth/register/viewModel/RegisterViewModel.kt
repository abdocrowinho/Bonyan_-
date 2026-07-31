package org.muslim_voice.project.features.auth.register.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.auth.GoogleAccountInfo
import org.muslim_voice.project.core.constants.PrayerType
import org.muslim_voice.project.core.data.getAllCountries
import org.muslim_voice.project.core.data.repository.PrayerSoundRepository
import org.muslim_voice.project.core.domain.model.auth.register.request.RecordingsModel
import org.muslim_voice.project.core.domain.model.auth.register.request.RegisterRequestModel
import org.muslim_voice.project.core.domain.repository.AuthStorageRepository
import org.muslim_voice.project.core.domain.usecase.RegisterUseCase
import org.muslim_voice.project.core.domain.util.ApiResult
import org.muslim_voice.project.core.logging.AppLog
import org.muslim_voice.project.core.microphone.RecordedAudio
import org.muslim_voice.project.core.mvi.BaseViewModel
import org.muslim_voice.project.core.utilities.toUiText
import org.muslim_voice.project.features.auth.register.event.RegisterEvent
import org.muslim_voice.project.features.auth.register.event.RegisterEvent.*
import org.muslim_voice.project.features.auth.register.intent.RegisterIntent
import org.muslim_voice.project.features.auth.register.state.RegisterState
import org.muslim_voice.project.features.auth.register.ui_Model.RegisterStep

class RegisterViewModel(
    private val repository: PrayerSoundRepository,
    private val registerUseCase: RegisterUseCase,
    private val authStorageRepository: AuthStorageRepository,
) : BaseViewModel<RegisterState, RegisterIntent, RegisterEvent>(
    RegisterState(countryOptions = getAllCountries())
) {

    fun setGoogleAccount(account: GoogleAccountInfo?) {
        if (account == null || currentState().googleAccount == account) return

        val displayName = account.displayName.orEmpty().trim()
        val googleFirstName = displayName.substringBefore(' ', "")
        val googleLastName = displayName.substringAfter(' ', missingDelimiterValue = "")

        updateState { currentState ->
            val currentInfo = currentState.registerInfo

            currentState.copy(
                googleAccount = account,
                registerInfo = currentInfo.copy(
                    email = account.email,
                    firstName = currentInfo.firstName.ifBlank { googleFirstName },
                    lastName = currentInfo.lastName.ifBlank { googleLastName },
                )
            )
        }
    }

    override fun handleIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.OnFirstNameChanged -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(firstName = intent.value),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(FIRST_NAME) },
                )
            }

            is RegisterIntent.OnLastNameChanged -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(lastName = intent.value),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(LAST_NAME) },
                )
            }

            is RegisterIntent.OnEmailChanged -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(email = intent.value),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(EMAIL) },
                )
            }

            is RegisterIntent.OnPasswordChanged -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(password = intent.value),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(PASSWORD) },
                )
            }

            is RegisterIntent.OnBirthDateSelected -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(birthDate = intent.date),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(BIRTH_DATE) },
                )
            }

            is RegisterIntent.OnRoleModelChanged -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(roleModel = intent.value),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(ROLE_MODEL) },
                )
            }

            is RegisterIntent.OnFavoriteSurahChanged -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(favoriteSurah = intent.value),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(FAVORITE_SURAH) },
                )
            }

            is RegisterIntent.OnFavoriteAyahChanged -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(favoriteAyah = intent.value),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(FAVORITE_AYAH) },
                )
            }

            is RegisterIntent.OnCountrySelected -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(selectedCountry = intent.option),
                    step1Errors = it.step1Errors.toMutableMap().apply { remove(COUNTRY) },
                )
            }

            RegisterIntent.OnStep1NextClicked -> validateStep1AndContinue()

            is RegisterIntent.OnStartRecording -> {
                val state = currentState()
                updateState {
                    it.copy(
                        currentlyRecording = intent.prayType,
                        currentlyPlaying = null,
                    )
                }
                sendEvent(
                    StartPrayerRecording(
                        prayType = intent.prayType,
                        recordingToStop = state.currentlyRecording?.takeIf { it != intent.prayType },
                        playbackToStop = state.currentlyPlaying,
                    )
                )
            }

            is RegisterIntent.OnStopRecording -> {
                if (currentState().currentlyRecording == intent.prayType) {
                    sendEvent(StopPrayerRecording(intent.prayType))
                }
            }

            is RegisterIntent.OnPlayRecording -> {
                val state = currentState()
                val audioPath = state.registerInfo.recordings[intent.prayType]
                if (audioPath == null) {
                    sendEvent(ShowError("No recording found for this prayer"))
                    return
                }

                updateState {
                    it.copy(
                        currentlyRecording = null,
                        currentlyPlaying = intent.prayType,
                    )
                }
                sendEvent(
                    PlayPrayerRecording(
                        prayType = intent.prayType,
                        audioPath = audioPath,
                        recordingToStop = state.currentlyRecording,
                        playbackToStop = state.currentlyPlaying?.takeIf { it != intent.prayType },
                    )
                )
            }

            is RegisterIntent.OnRecordingCaptured -> {
                updateState {
                    it.copy(
                        registerInfo = it.registerInfo.copy(
                            recordings = it.registerInfo.recordings + (intent.prayType to intent.audioPath)
                        ),
                        currentlyRecording = null,
                        currentlyPlaying = null,
                    )
                }
                viewModelScope.launch {
                    repository.save(
                        intent.prayType,
                        RecordedAudio(intent.audioPath, intent.durationMillis),
                    )
                }
            }

            is RegisterIntent.OnPlaybackFinished -> updateState {
                if (it.currentlyPlaying == intent.prayType) {
                    it.copy(currentlyPlaying = null)
                } else {
                    it
                }
            }

            is RegisterIntent.OnPrayerAudioError -> {
                updateState {
                    it.copy(
                        currentlyRecording = null,
                        currentlyPlaying = null,
                    )
                }
                sendEvent(ShowError(intent.message))
            }

            RegisterIntent.OnStep2NextClicked -> updateState {
                it.copy(currentStep = RegisterStep.PROFILE_PICTURE)
            }

            is RegisterIntent.OnImageSelected -> updateState {
                it.copy(
                    registerInfo = it.registerInfo.copy(profileImageBytes = intent.bytes)
                )
            }

            RegisterIntent.OnBackStepClicked -> moveBack()
            RegisterIntent.SubmitProfile -> submitProfile()
        }
    }

    private fun validateStep1AndContinue() {
        val info = currentState().registerInfo
        val errors = buildMap {
            if (info.firstName.isBlank()) put(FIRST_NAME, "First name is required")
            if (info.lastName.isBlank()) put(LAST_NAME, "Last name is required")
            if (info.email.isBlank()) put(EMAIL, "Email is required")
            if (info.password.isBlank()) put(PASSWORD, "Password is required")
            if (info.birthDate == null) put(BIRTH_DATE, "Birth date is required")
            if (info.roleModel.isBlank()) put(ROLE_MODEL, "Role model is required")
            if (info.favoriteSurah.isBlank()) put(FAVORITE_SURAH, "Favorite surah is required")
            if (info.favoriteAyah.isBlank()) put(FAVORITE_AYAH, "Favorite ayah is required")
            if (info.selectedCountry == null) put(COUNTRY, "Country is required")
        }

        if (errors.isNotEmpty()) {
            updateState { it.copy(step1Errors = errors) }
        } else {
            updateState {
                it.copy(
                    step1Errors = emptyMap(),
                    currentStep = RegisterStep.PRAYER_VOICES,
                )
            }
        }
    }

    private fun submitProfile() {
        val state = currentState()
        val info = state.registerInfo
        val birthDate = info.birthDate
        val country = info.selectedCountry

        if (birthDate == null || country == null) {
            sendEvent(ShowError("Profile data is incomplete"))
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }

            val mediaResult = authStorageRepository.uploadRegisterMedia(
                profileImageBytes = info.profileImageBytes,
                recordingPaths = info.recordings,
            )

            mediaResult
                .onFailure { error ->
                    AppLog.e("RegisterViewModel", "Failed to upload register media", error)
                    updateState { it.copy(isLoading = false) }
                    sendEvent(ShowError(error.message ?: "Failed to upload media"))
                }
                .onSuccess { uploadedMedia ->
                    val request = RegisterRequestModel(
                        firstName = info.firstName.trim(),
                        lastName = info.lastName.trim(),
                        email = info.email.ifBlank { state.googleAccount?.email.orEmpty() }.trim(),
                        password = info.password,
                        favoriteAyah = info.favoriteAyah.trim(),
                        favoriteSurah = info.favoriteSurah.trim(),
                        country = country.id,
                        birthDate = birthDate.toString(),
                        roleModel = info.roleModel.trim(),
                        profilePicture = uploadedMedia.profilePictureUrl,
                        recordings = uploadedMedia.recordingUrls.toRecordingsModel(),
                    )

                    registerUseCase(request).collect { result ->
                        when (result) {
                            ApiResult.Loading -> updateState { it.copy(isLoading = true) }
                            is ApiResult.Success -> {
                                updateState { it.copy(isLoading = false) }
                                sendEvent(NavigateToOtp(result.data.email.ifBlank { request.email }))
                            }
                            is ApiResult.Failure -> {
                                AppLog.e("RegisterViewModel", "Register request failed: ${result.error}")
                                updateState { it.copy(isLoading = false) }
                                sendEvent(ShowError(result.error.toUiText()))
                            }
                        }
                    }
                }
        }
    }

    private fun Map<PrayerType, String>.toRecordingsModel(): RecordingsModel {
        return RecordingsModel(
            fajr = this[PrayerType.FAJR],
            dhuhr = this[PrayerType.DHUHR],
            asr = this[PrayerType.ASR],
            maghrib = this[PrayerType.MAGHRIB],
            isha = this[PrayerType.ISHA],
        )
    }

    private fun moveBack() {
        updateState {
            val previousStep = when (it.currentStep) {
                RegisterStep.PERSONAL_INFO -> RegisterStep.PERSONAL_INFO
                RegisterStep.PRAYER_VOICES -> RegisterStep.PERSONAL_INFO
                RegisterStep.PROFILE_PICTURE -> RegisterStep.PRAYER_VOICES
            }
            it.copy(currentStep = previousStep)
        }
    }

    private companion object {
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val BIRTH_DATE = "birthDate"
        const val ROLE_MODEL = "roleModel"
        const val FAVORITE_SURAH = "favoriteSurah"
        const val FAVORITE_AYAH = "favoriteAyah"
        const val COUNTRY = "country"
    }
}