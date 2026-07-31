package org.muslim_voice.project.features.auth.otpScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.muslim_voice.project.core.domain.model.auth.otp.request.OtpRequestModel
import org.muslim_voice.project.core.domain.usecase.OtpUseCase
import org.muslim_voice.project.core.domain.util.ApiResult
import org.muslim_voice.project.core.utilities.toUiText

class OtpViewModel(
    private val otpUseCase: OtpUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(OtpState())
    val state: StateFlow<OtpState> = _state.asStateFlow()

    private val _event = Channel<OtpEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    fun setEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onOtpChanged(value: String) {
        _state.update {
            it.copy(
                otpCode = value.filter(Char::isDigit).take(6),
                otpError = null,
            )
        }
    }

    fun verifyOtp() {
        val current = state.value
        viewModelScope.launch {
            otpUseCase(
                OtpRequestModel(
                    email = current.email.trim(),
                    otpCode = current.otpCode.trim(),
                )
            ).collect { result ->
                when (result) {
                    ApiResult.Loading -> _state.update { it.copy(isLoading = true, otpError = null) }
                    is ApiResult.Success -> {
                        _state.update { it.copy(isLoading = false, showSuccessDialog = true) }
                    }
                    is ApiResult.Failure -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                otpError = result.error.toUiText(),
                            )
                        }
                    }
                }
            }
        }
    }

    fun dismissSuccessDialog() {
        _state.update { it.copy(showSuccessDialog = false) }
        viewModelScope.launch {
            _event.send(OtpEvent.NavigateToLogin)
        }
    }
}

sealed interface OtpEvent {
    data object NavigateToLogin : OtpEvent
}
