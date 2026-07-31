package org.muslim_voice.project.features.auth.register.intent

import kotlinx.datetime.LocalDate
import org.muslim_voice.project.core.component.DropdownOption
import org.muslim_voice.project.core.constants.PrayerType

sealed interface RegisterIntent {
    data class OnFirstNameChanged(val value: String) : RegisterIntent
    data class OnLastNameChanged(val value: String) : RegisterIntent
    data class OnBirthDateSelected(val date: LocalDate) : RegisterIntent
    data class OnRoleModelChanged(val value: String) : RegisterIntent
    data class OnFavoriteSurahChanged(val value: String) : RegisterIntent
    data class OnFavoriteAyahChanged(val value: String) : RegisterIntent
    data class OnCountrySelected(val option: DropdownOption) : RegisterIntent
    data class OnEmailChanged(val value: String) : RegisterIntent
    data class OnPasswordChanged(val value: String) : RegisterIntent
    data object OnStep1NextClicked : RegisterIntent
    data class OnStartRecording(val prayType: PrayerType) : RegisterIntent
    data class OnStopRecording(val prayType: PrayerType) : RegisterIntent
    data class OnPlayRecording(val prayType: PrayerType) : RegisterIntent
    data class OnRecordingCaptured(
        val prayType: PrayerType,
        val audioPath: String,
        val durationMillis: Long,
    ) : RegisterIntent
    data class OnPlaybackFinished(val prayType: PrayerType) : RegisterIntent
    data class OnPrayerAudioError(val message: String) : RegisterIntent
    data object OnStep2NextClicked : RegisterIntent
    data class OnImageSelected(val bytes: ByteArray) : RegisterIntent

    data object SubmitProfile : RegisterIntent
    data object OnBackStepClicked : RegisterIntent
}
