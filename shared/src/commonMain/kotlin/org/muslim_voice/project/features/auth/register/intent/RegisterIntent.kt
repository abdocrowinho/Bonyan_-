package org.muslim_voice.project.features.auth.register.intent

import kotlinx.datetime.LocalDate
import org.muslim_voice.project.core.component.DropdownOption
import org.muslim_voice.project.features.auth.register.ui_Model.PrayType

sealed interface RegisterIntent {
    data class OnFirstNameChanged(val value: String) : RegisterIntent
    data class OnLastNameChanged(val value: String) : RegisterIntent
    data class OnBirthDateSelected(val date: LocalDate) : RegisterIntent
    data class OnRoleModelChanged(val value: String) : RegisterIntent
    data class OnFavoriteSurahChanged(val value: String) : RegisterIntent
    data class OnFavoriteAyahChanged(val value: String) : RegisterIntent
    data class OnCountrySelected(val option: DropdownOption) : RegisterIntent
    data object OnStep1NextClicked : RegisterIntent
    data class OnStartRecording(val prayType: PrayType) : RegisterIntent
    data class OnStopRecording(val prayType: PrayType) : RegisterIntent
    data class OnPlayRecording(val prayType: PrayType) : RegisterIntent
    data class OnRecordingCaptured(val prayType: PrayType, val bytes: ByteArray) : RegisterIntent
    data object OnStep2NextClicked : RegisterIntent
    data object OnPickFromGalleryClicked : RegisterIntent
    data object OnCaptureFromCameraClicked : RegisterIntent
    data class OnProfileImageCaptured(val bytes: ByteArray) : RegisterIntent
    data object OnStep3NextClicked : RegisterIntent
    data class OnVerificationCodeChanged(val value: String) : RegisterIntent
    data object OnVerifyClicked : RegisterIntent
    data object OnResendCodeClicked : RegisterIntent
    data object OnBackStepClicked : RegisterIntent
}
