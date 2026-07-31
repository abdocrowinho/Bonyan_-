package org.muslim_voice.project.core.data.remote.mapper.auth.loginMapper.request


import org.muslim_voice.project.core.data.remote.dto.auth.login.request.LoginRequestDto
import org.muslim_voice.project.core.domain.model.auth.login.request.LoginRequestModel

fun LoginRequestModel.toDto(): LoginRequestDto {
    return LoginRequestDto(
        email = email,
        password = password
    )
}