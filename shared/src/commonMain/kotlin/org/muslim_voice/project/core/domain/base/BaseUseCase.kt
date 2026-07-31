package org.muslim_voice.project.core.domain.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.muslim_voice.project.core.domain.util.ApiResult
import org.muslim_voice.project.core.domain.util.NetworkError

abstract class BaseUseCase<in P, R> {

    protected open val validator: BaseValidation<@UnsafeVariance P>? = null

    operator fun invoke(param: P): Flow<ApiResult<R>> = flow {
        emit(ApiResult.Loading)

        // 2. Perform Validation
        val validation = validator?.validate(param) ?: ValidationResult.Success
        if (validation is ValidationResult.Failure) {
            emit(
                ApiResult.Failure(
                    error = NetworkError.ValidationError(
                        errors = validation.errors
                    )
                )
            )
            return@flow
        }

        execute(param).collect { result ->
            emit(result)
        }
    }.catch { throwable ->
        emit(ApiResult.Failure(error = mapThrowableToNetworkError(throwable)))
    }

    protected abstract suspend fun execute(param: P): Flow<ApiResult<R>>
}

fun mapThrowableToNetworkError(e: Throwable): NetworkError {
    return NetworkError.Unknown(
        throwable = e,
        message = e.message ?: "An unexpected error occurred"
    )
}