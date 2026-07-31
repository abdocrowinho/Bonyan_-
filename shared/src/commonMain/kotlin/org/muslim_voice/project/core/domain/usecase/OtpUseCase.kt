package org.muslim_voice.project.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.domain.base.BaseUseCase
import org.muslim_voice.project.core.domain.model.auth.otp.request.OtpRequestModel
import org.muslim_voice.project.core.domain.model.auth.otp.response.OtpResponseModel
import org.muslim_voice.project.core.domain.repository.AuthRepository
import org.muslim_voice.project.core.domain.util.ApiResult
import org.muslim_voice.project.core.domain.validation.otp.OtpValidation

class OtpUseCase(
    private val authRepository: AuthRepository,
    override val validator: OtpValidation
) : BaseUseCase<OtpRequestModel, OtpResponseModel>() {
    override suspend fun execute(param: OtpRequestModel): Flow<ApiResult<OtpResponseModel>> {
        return authRepository.verifyOtp(param)
    }
}
