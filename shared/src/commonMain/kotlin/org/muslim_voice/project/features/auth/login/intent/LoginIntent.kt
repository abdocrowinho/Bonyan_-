package org.muslim_voice.project.features.auth.login.intent

sealed interface LoginIntent {
    data class OnEmailChanged(val value: String) : LoginIntent
    data class OnPasswordChanged(val value: String) : LoginIntent
    data object OnLoginClicked : LoginIntent
    data object OnGoogleSignInClicked : LoginIntent
    data object OnNavigateToRegisterClicked : LoginIntent
}
