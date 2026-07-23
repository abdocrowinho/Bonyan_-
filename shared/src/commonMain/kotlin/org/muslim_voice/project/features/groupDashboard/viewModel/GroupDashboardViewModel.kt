package org.muslim_voice.project.features.groupDashboard.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.muslim_voice.project.features.groupDashboard.effect.GroupDashboardEffect
import org.muslim_voice.project.features.groupDashboard.intent.GroupDashboardIntent
import org.muslim_voice.project.features.groupDashboard.state.GroupDashboardState
import org.muslim_voice.project.features.groupDashboard.state.ReadyStateUi

class GroupDashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(GroupDashboardState())
    val state: StateFlow<GroupDashboardState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<GroupDashboardEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<GroupDashboardEffect> = _effect.asSharedFlow()

    fun onIntent(intent: GroupDashboardIntent) {
        when (intent) {
            GroupDashboardIntent.ToggleReady -> _state.update { current ->
                val next = when (current.readyState) {
                    ReadyStateUi.IDLE -> ReadyStateUi.READY
                    ReadyStateUi.READY -> ReadyStateUi.DONE
                    ReadyStateUi.DONE -> ReadyStateUi.IDLE
                }
                current.copy(readyState = next)
            }
            GroupDashboardIntent.OpenNotifications -> viewModelScope.launch {
                _effect.emit(GroupDashboardEffect.NavigateToNotifications)
            }
            is GroupDashboardIntent.SetWalkiePressed -> _state.update {
                it.copy(isWalkiePressed = intent.pressed)
            }
        }
    }
}
