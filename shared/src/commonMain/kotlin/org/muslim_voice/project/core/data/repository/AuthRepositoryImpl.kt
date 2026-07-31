package org.muslim_voice.project.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.data.remote.api.AuthApiService
import org.muslim_voice.project.core.data.remote.dto.auth.forget_password.response.ForgetPasswordDto
import org.muslim_voice.project.core.data.remote.dto.auth.reset_password.request.ResetPasswordRequestDto
import org.muslim_voice.project.core.data.remote.mapper.auth.loginMapper.request.toDto
import org.muslim_voice.project.core.data.remote.mapper.auth.loginMapper.response.toDomain
import org.muslim_voice.project.core.data.remote.mapper.auth.otpMapper.request.toDto
import org.muslim_voice.project.core.data.remote.mapper.auth.otpMapper.response.toDomain
import org.muslim_voice.project.core.data.remote.mapper.auth.registerMapper.request.toDto
import org.muslim_voice.project.core.data.remote.mapper.auth.registerMapper.response.toDomain
import org.muslim_voice.project.core.data.util.executeApi

import org.muslim_voice.project.core.domain.model.auth.login.request.LoginRequestModel
import org.muslim_voice.project.core.domain.model.auth.login.response.LoginModel
import org.muslim_voice.project.core.domain.model.auth.otp.request.OtpRequestModel
import org.muslim_voice.project.core.domain.model.auth.otp.response.OtpResponseModel
import org.muslim_voice.project.core.domain.model.auth.register.request.RegisterRequestModel
import org.muslim_voice.project.core.domain.model.auth.register.response.RegisterResponseModel
import org.muslim_voice.project.core.domain.repository.AuthRepository
import org.muslim_voice.project.core.domain.util.ApiResult

class AuthRepositoryImpl(
    private val authApiService: AuthApiService
) : AuthRepository {
    override fun register(request: RegisterRequestModel): Flow<ApiResult<RegisterResponseModel>> {
        return executeApi {
            authApiService.register(request.toDto()).toDomain()
        }
    }

    override fun login(request: LoginRequestModel): Flow<ApiResult<LoginModel>> {
        return executeApi { authApiService.login(request.toDto()).data.toDomain()}
    }

    override fun verifyOtp(request: OtpRequestModel): Flow<ApiResult<OtpResponseModel>> {
        return executeApi { authApiService.verifyOtp(request.toDto()).data.toDomain()}
    }

    override fun forgetPassword(request: ForgetPasswordDto): Flow<ApiResult<Unit>> {
        return executeApi { authApiService.forgetPassword(request).data }
    }

    override fun resetPassword(request: ResetPasswordRequestDto): Flow<ApiResult<ForgetPasswordDto>> {
        return executeApi { authApiService.resetPassword(request).data }
    }

}