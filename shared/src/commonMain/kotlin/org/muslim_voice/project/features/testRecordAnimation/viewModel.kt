package org.muslim_voice.project.features.testRecordAnimation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────
//  V I E W   M O D E L
//  Holds state, dispatches intents through
//  the pure reducer. Platform-agnostic.
// ─────────────────────────────────────────────

class VoiceViewModel {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(VoiceState())
    val state: StateFlow<VoiceState> = _state.asStateFlow()

    // Platform audio engine is injected — not constructed here
    var audioEngine: AudioEngine? = null

    fun dispatch(intent: VoiceIntent) {
        _state.update { current -> VoiceReducer.reduce(current, intent) }

        // Side effects (only non-pure things live here)
        when (intent) {
            is VoiceIntent.StartRecording -> {
                scope.launch { audioEngine?.start(::dispatch) }
            }
            is VoiceIntent.StopRecording -> {
                scope.launch {
                    audioEngine?.stop()
                    // Short analysis delay, then emit result
                    kotlinx.coroutines.delay(600)
                    _state.update { it.copy(phase = RecordingPhase.Result) }
                }
            }
            is VoiceIntent.Reset -> {
                scope.launch { audioEngine?.stop() }
            }
            else -> Unit
        }
    }

    fun clear() {
        audioEngine?.stop()
        scope.cancel()
    }
}

// ─────────────────────────────────────────────
//  AudioEngine interface — implemented per platform
// ─────────────────────────────────────────────

interface AudioEngine {
    suspend fun start(dispatch: (VoiceIntent) -> Unit)
    fun stop()
}