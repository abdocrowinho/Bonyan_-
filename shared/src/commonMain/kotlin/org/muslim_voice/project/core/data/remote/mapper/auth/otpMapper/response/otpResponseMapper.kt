package org.muslim_voice.project.core.data.remote.mapper.auth.otpMapper.response

import org.muslim_voice.project.core.domain.model.auth.otp.response.OtpResponseModel
import org.muslim_voice.project.core.data.remote.dto.auth.otp.response.OtpResponseDto


fun OtpResponseDto.toDomain(): OtpResponseModel {
    return OtpResponseModel(
        email = email.orEmpty(),
        isVerified = isVerified ?: false
    )
}