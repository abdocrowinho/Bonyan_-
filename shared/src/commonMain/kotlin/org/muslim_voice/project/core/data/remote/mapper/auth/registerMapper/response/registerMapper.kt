package org.muslim_voice.project.core.data.remote.mapper.auth.registerMapper.response

import org.muslim_voice.project.core.domain.model.auth.register.response.RegisterResponseModel


import org.muslim_voice.project.core.data.remote.dto.auth.register.response.RegisterResponseDto

fun RegisterResponseDto.toDomain(): RegisterResponseModel {
    return RegisterResponseModel(
        email = email.orEmpty(),
        resendId = resendId.orEmpty(),
        message = message.orEmpty()
    )
}