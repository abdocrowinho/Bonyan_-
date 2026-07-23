package org.muslim_voice.project.features.pickLocationScreen.intent

import org.muslim_voice.project.core.permission.PermissionStatus


sealed interface LocationPermissionIntent {
    data object OnAllowLocationClicked : LocationPermissionIntent
    data object OnSkipClicked : LocationPermissionIntent
    data class OnPermissionResult(val status: PermissionStatus) : LocationPermissionIntent
}