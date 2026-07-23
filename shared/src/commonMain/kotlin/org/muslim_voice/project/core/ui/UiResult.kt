package org.muslim_voice.project.core.ui

sealed interface UiResult<out T> {
    data object Idle : UiResult<Nothing>
    data object Loading : UiResult<Nothing>
    data class Success<T>(val data: T) : UiResult<T>
    data class Error(val message: String) : UiResult<Nothing>
}
