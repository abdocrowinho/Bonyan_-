package org.muslim_voice.project.core.domain.model.auth.otp.response



data class OtpResponseModel(
    val email: String,
    val isVerified: Boolean
)