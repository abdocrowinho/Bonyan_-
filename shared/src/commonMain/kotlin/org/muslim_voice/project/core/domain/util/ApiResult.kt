package org.muslim_voice.project.core.domain.util


sealed interface ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>
    data class Failure(val error: NetworkError) : ApiResult<Nothing>
    data object Loading : ApiResult<Nothing>
}