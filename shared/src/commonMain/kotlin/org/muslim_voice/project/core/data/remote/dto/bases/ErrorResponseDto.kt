package org.muslim_voice.project.core.data.remote.dto.bases


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    @SerialName("success")
    val success: Boolean? = false,

    @SerialName("statusCode")
    val statusCode: Int? = null,

    @SerialName("error")
    val error: String? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("path")
    val path: String? = null,

    @SerialName("timestamp")
    val timestamp: String? = null
)