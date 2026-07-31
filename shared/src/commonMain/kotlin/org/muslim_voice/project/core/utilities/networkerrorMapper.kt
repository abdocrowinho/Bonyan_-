package org.muslim_voice.project.core.utilities

import org.muslim_voice.project.core.domain.util.NetworkError

fun NetworkError.toUiText(): String {
    return when (this) {
        NetworkError.NoInternet -> "Check your internet connection and try again"
        NetworkError.RequestTimeout -> "The request timed out, please try again"
        is NetworkError.Serialization -> "Failed to process server response"
        is NetworkError.ValidationError -> this.errors.values.flatten().firstOrNull()
            ?: "Validation error"
        is NetworkError.HttpError -> {
            when (this.code) {
                401 -> "Your session has expired, please log in again"
                403 -> "You do not have permission to access this resource"
                404 -> "The requested data was not found"
                else -> this.serverMessage ?: "Server error (${this.code})"
            }
        }
        is NetworkError.Unknown -> this.message ?: "Unexpected error"
    }
}
