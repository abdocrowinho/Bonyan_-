package org.muslim_voice.project.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Intent, Event>(
    initialState: State,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _event = Channel<Event>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    protected fun updateState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    protected fun setState(newState: State) {
        _state.value = newState
    }

    protected fun currentState(): State = _state.value

    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    abstract fun handleIntent(intent: Intent)
}
