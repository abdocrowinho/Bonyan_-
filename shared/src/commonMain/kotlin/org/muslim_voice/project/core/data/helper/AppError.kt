package org.muslim_voice.project.core.data.helper

sealed interface AppError {
    data object Connection : AppError
    data object Unknown : AppError
    data class Server(val message: String) : AppError
    data class Validation(val message: String) : AppError
}
