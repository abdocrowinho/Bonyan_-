package org.muslim_voice.project.core.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseDialogViewModel<State, Event>(
    initialState: State,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    protected fun updateState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    protected fun currentState(): State = _state.value

    abstract fun onEvent(event: Event)
}
