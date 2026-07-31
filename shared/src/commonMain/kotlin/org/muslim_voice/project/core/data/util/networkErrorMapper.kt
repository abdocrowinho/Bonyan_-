package org.muslim_voice.project.core.data.remote.util

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.muslim_voice.project.core.data.remote.dto.bases.ErrorResponseDto
import org.muslim_voice.project.core.domain.util.NetworkError


suspend fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is ClientRequestException,
        is ServerResponseException,
        is RedirectResponseException -> {
            val response = (this as? ResponseException)?.response
            val statusCode = response?.status?.value ?: 500

            val errorDto = runCatching {
                response?.body<ErrorResponseDto>()
            }.getOrNull()

            NetworkError.HttpError(
                code = statusCode,
                errorCode = errorDto?.error,
                serverMessage = errorDto?.message ?: this.message
            )
        }

        is SerializationException -> NetworkError.Serialization(
            message = this.message ?: "Failed to parse server response"
        )

        is IOException -> NetworkError.NoInternet

        else -> NetworkError.Unknown(
            throwable = this,
            message = this.message ?: "An unexpected error occurred"
        )
    }
}