package org.muslim_voice.project.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNavigator {
    private val _navEvents = MutableSharedFlow<NavigationIntent>(
        extraBufferCapacity = 1,
        replay = 1,
    )
    val navEvents = _navEvents.asSharedFlow()

    private val _innerNavEvents = MutableSharedFlow<NavigationIntent>(extraBufferCapacity = 1)
    val innerNavEvents = _innerNavEvents.asSharedFlow()

    fun navigateTo(route: Any, popUpTo: Any? = null, inclusive: Boolean = false) {
        _innerNavEvents.tryEmit(NavigationIntent.To(route, popUpTo, inclusive))
    }

    fun navigateToOuter(route: Any, popUpTo: Any? = null, inclusive: Boolean = false) {
        _navEvents.tryEmit(NavigationIntent.To(route, popUpTo, inclusive))
    }

    fun popBack() {
        _innerNavEvents.tryEmit(NavigationIntent.Back)
    }
}
