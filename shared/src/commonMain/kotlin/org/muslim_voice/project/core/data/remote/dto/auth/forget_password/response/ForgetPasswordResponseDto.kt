package org.muslim_voice.project.core.data.remote.dto.auth.forget_password.response

import kotlinx.serialization.Serializable

@Serializable
data class ForgetPasswordDto(
	val email: String? = null
)

