package org.muslim_voice.project.features.testRecordAnimation

// ─────────────────────────────────────────────
//  M O D E L   (single source of truth)
// ─────────────────────────────────────────────

data class VoiceState(
    val phase: RecordingPhase       = RecordingPhase.Idle,
    val mood: VoiceMood             = VoiceMood.Idle,
    val analytics: VoiceAnalytics   = VoiceAnalytics(),
    val moodScores: MoodScores      = MoodScores(),
    val recordingSeconds: Int       = 0,
    val barHistory: List<Float>     = List(50) { 0f },
    val freqBins: List<Float>       = List(32) { 0f },
    val errorMessage: String?       = null,
)

// ── Phase ──────────────────────────────────────
enum class RecordingPhase {
    Idle, Recording, Analyzing, Result
}

// ── Mood ───────────────────────────────────────
enum class VoiceMood {
    Idle, Angry, Friendly, Smooth, Noisy;

    val label: String
        get() = when (this) {
            Idle     -> "Idle"
            Angry    -> "Angry"
            Friendly -> "Friendly"
            Smooth   -> "Smooth"
            Noisy    -> "Noisy"
        }

    val description: String
        get() = when (this) {
            Idle     -> "Ready to record"
            Angry    -> "Loud & aggressive"
            Friendly -> "Warm & welcoming"
            Smooth   -> "Calm & serene"
            Noisy    -> "Chaotic & distorted"
        }
}

// ── Raw audio signals (platform fills these) ──
data class VoiceAnalytics(
    val volume: Float   = 0f,   // 0–100   RMS loudness
    val pitch: Float    = 0f,   // Hz      dominant frequency
    val energy: Float   = 0f,   // 0–100   high/low freq ratio
    val centroid: Float = 0f,   // 0–100   spectral brightness
)

// ── Mood confidence scores ─────────────────────
data class MoodScores(
    val angry: Float    = 0f,
    val friendly: Float = 0f,
    val smooth: Float   = 0f,
    val noisy: Float    = 0f,
) {
    val total: Float get() = angry + friendly + smooth + noisy

    fun percent(mood: VoiceMood): Int {
        if (total == 0f) return 0
        val raw = when (mood) {
            VoiceMood.Angry    -> angry
            VoiceMood.Friendly -> friendly
            VoiceMood.Smooth   -> smooth
            VoiceMood.Noisy    -> noisy
            VoiceMood.Idle     -> 0f
        }
        return ((raw / total) * 100).toInt()
    }

    fun topMood(): VoiceMood {
        if (total < 5f) return VoiceMood.Idle
        return mapOf(
            VoiceMood.Angry    to angry,
            VoiceMood.Friendly to friendly,
            VoiceMood.Smooth   to smooth,
            VoiceMood.Noisy    to noisy,
        ).maxByOrNull { it.value }?.key ?: VoiceMood.Idle
    }

    /** Exponential moving average toward target */
    fun lerp(target: MoodScores, factor: Float = 0.15f) = MoodScores(
        angry    = angry    + (target.angry    - angry)    * factor,
        friendly = friendly + (target.friendly - friendly) * factor,
        smooth   = smooth   + (target.smooth   - smooth)   * factor,
        noisy    = noisy    + (target.noisy    - noisy)    * factor,
    )
}