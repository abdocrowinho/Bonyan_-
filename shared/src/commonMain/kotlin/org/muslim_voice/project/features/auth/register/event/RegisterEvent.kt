package org.muslim_voice.project.features.auth.register.event

import org.muslim_voice.project.core.constants.PrayerType


sealed interface RegisterEvent {
    data class StartPrayerRecording(
        val prayType: PrayerType,
        val recordingToStop: PrayerType?,
        val playbackToStop: PrayerType?,
    ) : RegisterEvent

    data class StopPrayerRecording(val prayType: PrayerType) : RegisterEvent

    data class PlayPrayerRecording(
        val prayType: PrayerType,
        val audioPath: String,
        val recordingToStop: PrayerType?,
        val playbackToStop: PrayerType?,
    ) : RegisterEvent

    data class NavigateToOtp(val email: String) : RegisterEvent
    data class ShowError(val message: String) : RegisterEvent
}
