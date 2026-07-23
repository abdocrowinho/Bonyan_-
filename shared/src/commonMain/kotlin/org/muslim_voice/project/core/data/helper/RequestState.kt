package org.muslim_voice.project.core.data.helper

sealed interface RequestState<out T> {
    data class Loading(val isLoading: Boolean = true) : RequestState<Nothing>
    data class Success<T>(
        val data: T,
        val isFromCache: Boolean = false,
    ) : RequestState<T>
    data class Failure(val error: AppError) : RequestState<Nothing>
}
