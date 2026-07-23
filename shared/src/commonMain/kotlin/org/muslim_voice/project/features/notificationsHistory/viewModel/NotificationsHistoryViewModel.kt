package org.muslim_voice.project.features.notificationsHistory.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.muslim_voice.project.features.notificationsHistory.intent.NotificationsHistoryIntent
import org.muslim_voice.project.features.notificationsHistory.state.NotificationsHistoryState

class NotificationsHistoryViewModel : ViewModel() {

    private val _state = MutableStateFlow(NotificationsHistoryState())
    val state: StateFlow<NotificationsHistoryState> = _state.asStateFlow()

    fun onIntent(intent: NotificationsHistoryIntent) {
        when (intent) {
            is NotificationsHistoryIntent.Dismiss -> _state.update { current ->
                current.copy(items = current.items.filterNot { it.id == intent.id })
            }
        }
    }
}
