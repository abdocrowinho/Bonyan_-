package org.muslim_voice.project.features.pickLocationScreen.viewModel


import org.muslim_voice.project.core.mvi.BaseViewModel
import org.muslim_voice.project.features.pickLocationScreen.event.LocationPermissionEvent
import org.muslim_voice.project.features.pickLocationScreen.intent.LocationPermissionIntent
import org.muslim_voice.project.features.pickLocationScreen.state.LocationPermissionState

class LocationPermissionViewModel :
    BaseViewModel<LocationPermissionState, LocationPermissionIntent, LocationPermissionEvent>(
        initialState = LocationPermissionState()
    ) {

    override fun handleIntent(intent: LocationPermissionIntent) {
        when (intent) {
            LocationPermissionIntent.OnAllowLocationClicked -> {
                sendEvent(LocationPermissionEvent.RequestSystemLocationPermission)
            }

            LocationPermissionIntent.OnSkipClicked,
            is LocationPermissionIntent.OnPermissionResult -> {
                sendEvent(LocationPermissionEvent.NavigateToNextScreen)

            }
        }
    }
}