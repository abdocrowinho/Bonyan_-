package org.muslim_voice.project.core.data.remote.dto.auth.login.request

import kotlinx.serialization.Serializable


@Serializable
data class LoginRequestDto(
	val password: String? = null,
	val email: String? = null
)
