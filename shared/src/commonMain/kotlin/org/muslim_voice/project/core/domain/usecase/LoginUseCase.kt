package org.muslim_voice.project.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.domain.base.BaseUseCase
import org.muslim_voice.project.core.domain.model.auth.login.request.LoginRequestModel
import org.muslim_voice.project.core.domain.model.auth.login.response.LoginModel
import org.muslim_voice.project.core.domain.repository.AuthRepository
import org.muslim_voice.project.core.domain.util.ApiResult
import org.muslim_voice.project.core.domain.validation.login.LoginValidation

class LoginUseCase(
    private val authRepository: AuthRepository,
    override val validator: LoginValidation
) : BaseUseCase<LoginRequestModel, LoginModel>() {
    override suspend fun execute(param: LoginRequestModel): Flow<ApiResult<LoginModel>> {
        return authRepository.login(param)
    }
}