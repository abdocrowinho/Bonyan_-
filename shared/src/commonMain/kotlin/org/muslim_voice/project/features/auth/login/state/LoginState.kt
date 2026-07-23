package org.muslim_voice.project.features.auth.login.state

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
)
