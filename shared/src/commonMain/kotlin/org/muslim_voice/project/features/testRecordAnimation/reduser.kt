package org.muslim_voice.project.features.testRecordAnimation

// ─────────────────────────────────────────────
//  R E D U C E R   (pure — no side effects)
// ─────────────────────────────────────────────

object VoiceReducer {

    fun reduce(state: VoiceState, intent: VoiceIntent): VoiceState = when (intent) {

        is VoiceIntent.StartRecording -> state.copy(
            phase            = RecordingPhase.Recording,
            recordingSeconds = 0,
            errorMessage     = null,
            barHistory       = List(50) { 0f },
            freqBins         = List(32) { 0f },
        )

        is VoiceIntent.StopRecording -> state.copy(
            phase = RecordingPhase.Analyzing,
        )

        is VoiceIntent.TickSecond -> state.copy(
            recordingSeconds = intent.seconds,
        )

        is VoiceIntent.AudioFrame -> {
            // Smooth scores via lerp then pick top mood live
            val smoothed = state.moodScores.lerp(intent.scores)
            state.copy(
                analytics  = intent.analytics,
                moodScores = smoothed,
                mood       = smoothed.topMood(),
                barHistory = intent.barHistory,
                freqBins   = intent.freqBins,
                phase      = RecordingPhase.Recording,
            )
        }

        is VoiceIntent.MicError -> state.copy(
            phase        = RecordingPhase.Idle,
            errorMessage = intent.message,
        )

        is VoiceIntent.Reset -> VoiceState()
    }
}