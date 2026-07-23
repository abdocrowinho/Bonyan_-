package org.muslim_voice.project.features.auth.register.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.auth.GoogleAccountInfo
import org.muslim_voice.project.core.data.UserPreferencesRepository
import org.muslim_voice.project.core.data.getAllCountries
import org.muslim_voice.project.core.domain.model.UserProfile
import org.muslim_voice.project.core.domain.repository.AuthRepository
import org.muslim_voice.project.core.mvi.BaseViewModel
import org.muslim_voice.project.features.auth.register.event.RegisterEvent
import org.muslim_voice.project.features.auth.register.intent.RegisterIntent
import org.muslim_voice.project.features.auth.register.state.RegisterState
import org.muslim_voice.project.features.auth.register.ui_Model.PrayType
import org.muslim_voice.project.features.auth.register.ui_Model.RegisterStep

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<RegisterState, RegisterIntent, RegisterEvent>(
    RegisterState(countryOptions = getAllCountries())
) {
    private var activeRecordingType: PrayType? = null

    fun setGoogleAccount(account: GoogleAccountInfo?) {
        if (account == null || currentState().googleAccount == account) return

        updateState {
            val displayName = account.displayName.orEmpty()
            it.copy(
                googleAccount = account,
                email = account.email,
                firstName = it.firstName.ifBlank { displayName.substringBefore(' ', "") },
                lastName = it.lastName.ifBlank { displayName.substringAfter(' ', "") },
            )
        }
    }

    override fun handleIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.OnFirstNameChanged -> updateState {
                it.copy(firstName = intent.value, step1Errors = it.step1Errors - FIRST_NAME)
            }

            is RegisterIntent.OnLastNameChanged -> updateState {
                it.copy(lastName = intent.value, step1Errors = it.step1Errors - LAST_NAME)
            }

            is RegisterIntent.OnBirthDateSelected -> updateState {
                it.copy(birthDate = intent.date, step1Errors = it.step1Errors - BIRTH_DATE)
            }

            is RegisterIntent.OnRoleModelChanged -> updateState {
                it.copy(roleModel = intent.value, step1Errors = it.step1Errors - ROLE_MODEL)
            }

            is RegisterIntent.OnFavoriteSurahChanged -> updateState {
                it.copy(favoriteSurah = intent.value, step1Errors = it.step1Errors - FAVORITE_SURAH)
            }

            is RegisterIntent.OnFavoriteAyahChanged -> updateState {
                it.copy(favoriteAyah = intent.value, step1Errors = it.step1Errors - FAVORITE_AYAH)
            }

            is RegisterIntent.OnCountrySelected -> updateState {
                it.copy(selectedCountry = intent.option, step1Errors = it.step1Errors - COUNTRY)
            }

            RegisterIntent.OnStep1NextClicked -> validateStep1AndContinue()

            is RegisterIntent.OnStartRecording -> {
                activeRecordingType = intent.prayType
                updateState { it.copy(currentlyRecording = intent.prayType) }
                sendEvent(RegisterEvent.RequestMicRecording)
            }

            is RegisterIntent.OnStopRecording -> {
                activeRecordingType = intent.prayType
                updateState { it.copy(currentlyRecording = null) }
                sendEvent(RegisterEvent.RequestMicRecording)
            }

            is RegisterIntent.OnPlayRecording -> {
                updateState { it.copy(currentlyPlaying = intent.prayType) }
                sendEvent(RegisterEvent.RequestMicRecording)
            }

            is RegisterIntent.OnRecordingCaptured -> updateState {
                it.copy(
                    recordings = it.recordings + (intent.prayType to intent.bytes),
                    currentlyRecording = null,
                    currentlyPlaying = null,
                )
            }

            RegisterIntent.OnStep2NextClicked -> updateState {
                it.copy(currentStep = RegisterStep.PROFILE_PICTURE)
            }

            RegisterIntent.OnPickFromGalleryClicked -> sendEvent(RegisterEvent.RequestGalleryPick)
            RegisterIntent.OnCaptureFromCameraClicked -> sendEvent(RegisterEvent.RequestCameraCapture)

            is RegisterIntent.OnProfileImageCaptured -> updateState {
                it.copy(profileImageBytes = intent.bytes)
            }

            RegisterIntent.OnStep3NextClicked -> {
                if (currentState().googleAccount != null) {
                    submitProfile()
                } else {
                    sendVerificationCodeAndContinue()
                }
            }

            is RegisterIntent.OnVerificationCodeChanged -> updateState {
                it.copy(verificationCode = intent.value, verificationError = null)
            }

            RegisterIntent.OnVerifyClicked -> verifyAndSubmit()
            RegisterIntent.OnResendCodeClicked -> sendVerificationCode(resend = true)
            RegisterIntent.OnBackStepClicked -> moveBack()
        }
    }

    fun consumeActiveRecordingType(): PrayType? = activeRecordingType

    private fun validateStep1AndContinue() {
        val state = currentState()
        val errors = buildMap {
            if (state.firstName.isBlank()) put(FIRST_NAME, "الاسم الأول مطلوب")
            if (state.lastName.isBlank()) put(LAST_NAME, "اسم العائلة مطلوب")
            if (state.birthDate == null) put(BIRTH_DATE, "تاريخ الميلاد مطلوب")
            if (state.roleModel.isBlank()) put(ROLE_MODEL, "القدوة مطلوبة")
            if (state.favoriteSurah.isBlank()) put(FAVORITE_SURAH, "السورة المفضلة مطلوبة")
            if (state.favoriteAyah.isBlank()) put(FAVORITE_AYAH, "الآية المفضلة مطلوبة")
            if (state.selectedCountry == null) put(COUNTRY, "الدولة مطلوبة")
        }

        if (errors.isNotEmpty()) {
            updateState { it.copy(step1Errors = errors) }
        } else {
            updateState { it.copy(step1Errors = emptyMap(), currentStep = RegisterStep.PRAYER_VOICES) }
        }
    }

    private fun sendVerificationCodeAndContinue() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            authRepository.sendVerificationCode(currentState().email)
                .onSuccess {
                    updateState { state ->
                        state.copy(currentStep = RegisterStep.VERIFICATION, isLoading = false)
                    }
                }
                .onFailure { error ->
                    updateState { state -> state.copy(isLoading = false) }
                    sendEvent(RegisterEvent.ShowError(error.message ?: "تعذر إرسال رمز التحقق"))
                }
        }
    }

    private fun sendVerificationCode(resend: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(isResendingCode = resend) }
            authRepository.sendVerificationCode(currentState().email)
                .onFailure { error ->
                    sendEvent(RegisterEvent.ShowError(error.message ?: "تعذر إرسال رمز التحقق"))
                }
            updateState { it.copy(isResendingCode = false) }
        }
    }

    private fun verifyAndSubmit() {
        val state = currentState()
        if (state.verificationCode.isBlank()) {
            updateState { it.copy(verificationError = "رمز التحقق مطلوب") }
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            authRepository.verifyCode(state.email, state.verificationCode)
                .onSuccess { submitProfile() }
                .onFailure { error ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            verificationError = error.message ?: "رمز التحقق غير صحيح",
                        )
                    }
                }
        }
    }

    private fun submitProfile() {
        val state = currentState()
        val birthDate = state.birthDate
        val country = state.selectedCountry

        if (birthDate == null || country == null) {
            sendEvent(RegisterEvent.ShowError("بيانات الملف الشخصي غير مكتملة"))
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            authRepository.submitProfile(
                UserProfile(
                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim(),
                    birthDate = birthDate,
                    roleModel = state.roleModel.trim(),
                    favoriteSurah = state.favoriteSurah.trim(),
                    favoriteAyah = state.favoriteAyah.trim(),
                    countryId = country.id,
                    email = state.email.ifBlank { state.googleAccount?.email.orEmpty() },
                    password = state.password.ifBlank { null },
                    googleAccount = state.googleAccount,
                    profileImageBytes = state.profileImageBytes,
                )
            ).onSuccess { token ->
                userPreferencesRepository.setSessionToken(token)
                sendEvent(RegisterEvent.NavigateToHome)
            }.onFailure { error ->
                sendEvent(RegisterEvent.ShowError(error.message ?: "تعذر إنشاء الحساب"))
            }
            updateState { it.copy(isLoading = false) }
        }
    }

    private fun moveBack() {
        updateState {
            val previousStep = when (it.currentStep) {
                RegisterStep.PERSONAL_INFO -> RegisterStep.PERSONAL_INFO
                RegisterStep.PRAYER_VOICES -> RegisterStep.PERSONAL_INFO
                RegisterStep.PROFILE_PICTURE -> RegisterStep.PRAYER_VOICES
                RegisterStep.VERIFICATION -> RegisterStep.PROFILE_PICTURE
            }
            it.copy(currentStep = previousStep)
        }
    }

    private companion object {
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val BIRTH_DATE = "birthDate"
        const val ROLE_MODEL = "roleModel"
        const val FAVORITE_SURAH = "favoriteSurah"
        const val FAVORITE_AYAH = "favoriteAyah"
        const val COUNTRY = "country"
    }
}
