package org.muslim_voice.project.features.testRecordAnimation

// ────────────────────────────────────────────
//  I N T E N T   (everything that can happen)
// ─────────────────────────────────────────────

sealed interface VoiceIntent {

    // User actions
    data object StartRecording : VoiceIntent
    data object StopRecording  : VoiceIntent
    data object Reset          : VoiceIntent

    // System/audio events
    data class TickSecond(val seconds: Int) : VoiceIntent

    // Audio frame: fired every ~60ms by the platform AudioEngine
    data class AudioFrame(
        val analytics:  VoiceAnalytics,
        val scores:     MoodScores,
        val barHistory: List<Float>,
        val freqBins:   List<Float>,
    ) : VoiceIntent

    // Errors
    data class MicError(val message: String) : VoiceIntent
}