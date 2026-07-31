package org.muslim_voice.project.core.domain.util

sealed interface NetworkError {

    data class HttpError(
        val code: Int,
        val errorCode: String?,
        val serverMessage: String?
    ) : NetworkError

    data object NoInternet : NetworkError

    data object RequestTimeout : NetworkError

    data class Serialization(val message: String) : NetworkError

    data class ValidationError(val errors: Map<String, List<String>>) : NetworkError

    data class Unknown(val throwable: Throwable? = null, val message: String? = null) : NetworkError
}