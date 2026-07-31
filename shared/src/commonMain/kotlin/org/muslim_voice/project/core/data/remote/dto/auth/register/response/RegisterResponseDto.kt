package org.muslim_voice.project.core.data.remote.dto.auth.register.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDto(
	val message: String? = null,
	val resendId: String? = null,
	val email: String? = null
)

