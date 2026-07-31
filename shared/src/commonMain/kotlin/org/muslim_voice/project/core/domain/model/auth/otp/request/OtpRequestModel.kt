package org.muslim_voice.project.core.domain.model.auth.otp.request

data class OtpRequestModel(
    val email: String,
    val otpCode: String
)
