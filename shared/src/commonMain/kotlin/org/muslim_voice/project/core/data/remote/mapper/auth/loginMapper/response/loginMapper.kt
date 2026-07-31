package org.muslim_voice.project.core.data.remote.mapper.auth.loginMapper.response

import org.muslim_voice.project.core.data.remote.dto.auth.login.response.LoginDataDto
import org.muslim_voice.project.core.data.remote.mapper.auth.userMapper.response.empty
import org.muslim_voice.project.core.data.remote.mapper.auth.userMapper.response.toDomain
import org.muslim_voice.project.core.domain.model.auth.login.response.LoginModel

fun LoginDataDto.toDomain(): LoginModel {
    return LoginModel(
        user = user?.toDomain()?.empty(),
        token = token.orEmpty()
    )
}