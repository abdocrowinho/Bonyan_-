package org.muslim_voice.project.features.testRecordAnimation

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// ─────────────────────────────────────────────
//  M O O D   S C O R E R
//  Pure function — signals → confidence scores
//  No state. No platform deps. 100% testable.
// ─────────────────────────────────────────────

object MoodScorer {

    /**
     * Four audio signals → four mood confidence scores (0–100 each).
     *
     * vol  = RMS loudness        0–100
     * pit  = dominant pitch      Hz
     * ene  = spectral energy     0–100  (high = harsh overtones)
     * cen  = spectral centroid   0–100  (high = bright/sharp timbre)
     */
    fun score(a: VoiceAnalytics): MoodScores {
        val vol = a.volume.coerceIn(0f, 100f)
        val pit = a.pitch
        val ene = a.energy.coerceIn(0f, 100f)
        val cen = a.centroid.coerceIn(0f, 100f)

        // ANGRY  — loud + high energy + bright spectrum
        val angry = clamp(vol * 0.40f + ene * 0.35f + cen * 0.25f)

        // FRIENDLY — mid-range pitch (150–280 Hz) + moderate energy + not too loud
        val pitFit = if (pit in 80f..320f) 100f - abs(pit - 200f) / 1.2f else 0f
        val friendly = clamp(pitFit * 0.45f + (100f - ene) * 0.30f + min(vol, 60f) * 0.80f)

        // SMOOTH — quiet + low energy + dark/round timbre
        val smooth = clamp((100f - vol) * 0.40f + (100f - ene) * 0.35f + (100f - cen) * 0.25f)

        // NOISY — high energy + bright + loud variance
        val noisy = clamp(ene * 0.50f + cen * 0.30f + max(0f, vol - 40f) * 0.80f)

        return MoodScores(angry = angry, friendly = friendly, smooth = smooth, noisy = noisy)
    }

    private fun clamp(v: Float) = v.coerceIn(0f, 100f)
}