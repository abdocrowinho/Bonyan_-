package org.muslim_voice.project.core.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.muslim_voice.project.core.data.remote.util.toNetworkError
import org.muslim_voice.project.core.domain.util.ApiResult

fun <T> executeApi(
    apiCall: suspend () -> T,
) = flow {
    emit(ApiResult.Loading)

    try {
        val rawResponse = apiCall()
        emit(ApiResult.Success(rawResponse))
    } catch (throwable: Throwable) {
        emit(ApiResult.Failure(throwable.toNetworkError()))
    }
}
