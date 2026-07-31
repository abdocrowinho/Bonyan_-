package org.muslim_voice.project.core.data.remote.mapper.auth.otpMapper.request

import org.muslim_voice.project.core.data.remote.dto.auth.otp.request.OtpRequestDto
import org.muslim_voice.project.core.domain.model.auth.otp.request.OtpRequestModel

fun OtpRequestModel.toDto(): OtpRequestDto {
    return OtpRequestDto(
        email = email!!,
        otpCode = otpCode
    )
}