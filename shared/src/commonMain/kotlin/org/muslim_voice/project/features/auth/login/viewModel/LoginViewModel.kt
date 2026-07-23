package org.muslim_voice.project.features.auth.login.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.auth.GoogleSignInController
import org.muslim_voice.project.core.data.UserPreferencesRepository
import org.muslim_voice.project.core.domain.repository.AuthRepository
import org.muslim_voice.project.core.mvi.BaseViewModel
import org.muslim_voice.project.features.auth.login.event.LoginEvent
import org.muslim_voice.project.features.auth.login.intent.LoginIntent
import org.muslim_voice.project.features.auth.login.state.LoginState

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val googleSignInController: GoogleSignInController,
) : BaseViewModel<LoginState, LoginIntent, LoginEvent>(LoginState()) {

    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnEmailChanged -> updateState {
                it.copy(email = intent.value, emailError = null)
            }

            is LoginIntent.OnPasswordChanged -> updateState {
                it.copy(password = intent.value, passwordError = null)
            }

            LoginIntent.OnLoginClicked -> loginWithEmail()
            LoginIntent.OnGoogleSignInClicked -> loginWithGoogle()
            LoginIntent.OnNavigateToRegisterClicked -> sendEvent(LoginEvent.NavigateToRegister)
        }
    }

    private fun loginWithEmail() {
        val email = currentState().email.trim()
        val password = currentState().password
        val emailError = validateEmail(email)
        val passwordError = if (password.isBlank()) "كلمة المرور مطلوبة" else null

        if (emailError != null || passwordError != null) {
            updateState {
                it.copy(emailError = emailError, passwordError = passwordError)
            }
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            authRepository.login(email, password)
                .onSuccess { token ->
                    userPreferencesRepository.setSessionToken(token)
                    sendEvent(LoginEvent.NavigateToHome)
                }
                .onFailure { error ->
                    sendEvent(LoginEvent.ShowError(error.message ?: "فشل تسجيل الدخول"))
                }
            updateState { it.copy(isLoading = false) }
        }
    }

    private fun loginWithGoogle() {
        viewModelScope.launch {
            updateState { it.copy(isGoogleLoading = true) }
            runCatching { googleSignInController.signIn() }
                .onSuccess { account ->
                    if (account == null) {
                        sendEvent(LoginEvent.ShowError("تم إلغاء تسجيل الدخول عبر Google"))
                    } else if (authRepository.hasCompletedProfile(account)) {
                        userPreferencesRepository.setSessionToken(account.idToken)
                        sendEvent(LoginEvent.NavigateToHome)
                    } else {
                        sendEvent(LoginEvent.NavigateToRegisterWithGoogleAccount(account))
                    }
                }
                .onFailure { error ->
                    sendEvent(LoginEvent.ShowError(error.message ?: "فشل تسجيل الدخول عبر Google"))
                }
            updateState { it.copy(isGoogleLoading = false) }
        }
    }

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) return "البريد الإلكتروني مطلوب"
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return if (emailRegex.matches(email)) null else "البريد الإلكتروني غير صالح"
    }
}
