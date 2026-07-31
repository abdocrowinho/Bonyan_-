package org.muslim_voice.project.features.auth.otpScreen

data class OtpState(
    val email: String="" ,
    val otpCode: String = "",
    val otpError: String? = null,
    val isLoading: Boolean = false,
    val showSuccessDialog: Boolean = false,
)
