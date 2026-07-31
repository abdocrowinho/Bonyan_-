package org.muslim_voice.project.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.domain.base.BaseUseCase
import org.muslim_voice.project.core.domain.base.BaseValidation
import org.muslim_voice.project.core.domain.model.auth.register.request.RegisterRequestModel
import org.muslim_voice.project.core.domain.model.auth.register.response.RegisterResponseModel
import org.muslim_voice.project.core.domain.repository.AuthRepository
import org.muslim_voice.project.core.domain.util.ApiResult
import org.muslim_voice.project.core.domain.validation.register.RegisterValidation

class RegisterUseCase(
    private val authRepository: AuthRepository,
    override val validator: RegisterValidation
) : BaseUseCase<RegisterRequestModel, RegisterResponseModel>() {
    override suspend fun execute(param: RegisterRequestModel): Flow<ApiResult<RegisterResponseModel>> {
        return authRepository.register(param)
    }
}