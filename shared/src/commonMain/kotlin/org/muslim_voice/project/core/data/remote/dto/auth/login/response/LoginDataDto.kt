package org.muslim_voice.project.core.data.remote.dto.auth.login.response

import kotlinx.serialization.Serializable
import org.muslim_voice.project.core.data.remote.dto.user.response.UserDto

@Serializable
data class LoginDataDto(
    val user: UserDto? = null,
    val token: String? = null
)
