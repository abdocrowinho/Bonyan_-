package org.muslim_voice.project.core.data.remote.dto.auth.otp.response

import kotlinx.serialization.Serializable

@Serializable
data class OtpResponseDto(
    val email : String?=null,
     val  isVerified : Boolean?=false
)
