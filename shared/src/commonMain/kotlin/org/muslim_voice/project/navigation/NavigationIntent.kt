package org.muslim_voice.project.navigation

sealed class NavigationIntent {
    data class To(
        val route: Any,
        val popUpTo: Any? = null,
        val inclusive: Boolean = false,
    ) : NavigationIntent()

    data object Back : NavigationIntent()
}
