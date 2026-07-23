package org.muslim_voice.project.core.data.helper

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
fun <T> executeApi(
    apiCall: suspend () -> T,
) = flow {
    emit(RequestState.Loading(true))
    try {
        val result = apiCall()
        emit(RequestState.Success(result))
    } catch (ex: CancellationException) {
        throw ex
    } catch (ex: Exception) {
        println("API_ERROR_DEBUG: ${ex.stackTraceToString()}")
        val appError = when (ex) {
            is ClientRequestException -> {
                val errorBody = runCatching { ex.response.bodyAsText() }.getOrElse { "" }
                decodeError(errorBody)
            }
            is ServerResponseException -> AppError.Unknown
            is RedirectResponseException -> AppError.Unknown
            else -> if (ex.isConnectionError()) AppError.Connection else AppError.Unknown
        }
        emit(RequestState.Failure(appError))
    }
}.catch { ex ->
    if (ex is CancellationException) throw ex
    emit(RequestState.Failure(AppError.Unknown))
}

private fun Exception.isConnectionError(): Boolean {
    val message = message.orEmpty()
    return message.contains("network", ignoreCase = true) ||
        message.contains("connection", ignoreCase = true) ||
        message.contains("Unable to resolve host", ignoreCase = true) ||
        message.contains("timeout", ignoreCase = true) ||
        (cause as? Exception)?.isConnectionError() == true
}

private fun decodeError(errorBody: String): AppError {
    if (errorBody.isBlank()) return AppError.Unknown
    return AppError.Server(errorBody)
}
