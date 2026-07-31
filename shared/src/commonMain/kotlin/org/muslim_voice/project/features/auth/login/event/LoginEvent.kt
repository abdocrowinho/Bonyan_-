package org.muslim_voice.project.features.auth.login.event

import org.muslim_voice.project.core.auth.GoogleAccountInfo

sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
    data object NavigateToRegister : LoginEvent
    data class NavigateToRegisterWithGoogleAccount(val account: GoogleAccountInfo) : LoginEvent
    data class ShowError(val message: String) : LoginEvent
    data class ValidationError(val errors: Map<String, List<String>>) : LoginEvent

}
