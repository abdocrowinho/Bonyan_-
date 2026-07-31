package org.muslim_voice.project.core.domain.model.auth.login.response

import org.muslim_voice.project.core.domain.model.auth.user.UserModel

data class LoginModel(
    val user: UserModel?,
    val token: String
)