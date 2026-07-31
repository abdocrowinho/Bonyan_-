package org.muslim_voice.project.core.data.remote.dto.auth.otp.request

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequestDto(
    val email: String,
    val otpCode: String
)
