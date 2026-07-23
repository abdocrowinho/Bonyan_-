package org.muslim_voice.project.features.pickLocationScreen.event

sealed interface LocationPermissionEvent {
    data object RequestSystemLocationPermission : LocationPermissionEvent
    data object NavigateToNextScreen : LocationPermissionEvent
}