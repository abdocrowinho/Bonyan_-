package org.muslim_voice.project.core.data.remote.dto.auth.reset_password.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestDto(
	val otpCode: String? = null,
	val newPassword: String? = null,
	val confirmPassword: String? = null,
	val email: String? = null
)

