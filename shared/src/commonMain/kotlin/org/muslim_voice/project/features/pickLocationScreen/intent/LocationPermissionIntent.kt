package org.muslim_voice.project.features.pickLocationScreen.intent



sealed interface LocationPermissionIntent {
    data object OnAllowLocationClicked : LocationPermissionIntent
    data object OnSkipClicked : LocationPermissionIntent
    data class OnPermissionResult(val status: String) : LocationPermissionIntent
}