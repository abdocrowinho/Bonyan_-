package org.muslim_voice.project.features.auth.login.viewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.auth.GoogleSignInController
import org.muslim_voice.project.core.data.UserPreferencesRepository
import org.muslim_voice.project.core.domain.model.auth.login.request.LoginRequestModel
import org.muslim_voice.project.core.domain.usecase.LoginUseCase
import org.muslim_voice.project.core.domain.util.ApiResult
import org.muslim_voice.project.core.domain.util.NetworkError
import org.muslim_voice.project.core.mvi.BaseViewModel
import org.muslim_voice.project.core.utilities.toUiText
import org.muslim_voice.project.features.auth.login.event.LoginEvent
import org.muslim_voice.project.features.auth.login.intent.LoginIntent
import org.muslim_voice.project.features.auth.login.state.LoginState

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
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

        viewModelScope.launch {
            loginUseCase(LoginRequestModel(email = email, password = password))
                .collect { result ->
                    when (result) {
                        ApiResult.Loading -> updateState {
                            it.copy(isLoading = true, emailError = null, passwordError = null)
                        }
                        is ApiResult.Success -> {
                            userPreferencesRepository.setSessionToken(result.data?.token)
                            updateState { it.copy(isLoading = false) }
                            sendEvent(LoginEvent.NavigateToHome)
                        }
                        is ApiResult.Failure -> {
                            updateState { it.copy(isLoading = false) }
                            when(result.error){
                                is NetworkError.ValidationError -> {
                                    sendEvent(LoginEvent.ValidationError(result.error.errors))

                                }
                                else -> {
                                    sendEvent(LoginEvent.ShowError(result.error.toUiText()))

                                }
                            }

                        }
                    }
                }
        }
    }

    private fun loginWithGoogle() {
        viewModelScope.launch {
            updateState { it.copy(isGoogleLoading = true) }
            runCatching { googleSignInController.signIn() }
                .onSuccess { account ->
                    if (account == null) {
                        sendEvent(LoginEvent.ShowError("Google sign-in was cancelled"))
                    } else {
                        sendEvent(LoginEvent.NavigateToRegisterWithGoogleAccount(account))
                    }
                }
                .onFailure { error ->
                    sendEvent(LoginEvent.ShowError(error.message ?: "Google sign-in failed"))
                }
            updateState { it.copy(isGoogleLoading = false) }
        }
    }
}
