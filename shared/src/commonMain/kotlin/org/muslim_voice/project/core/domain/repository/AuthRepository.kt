package org.muslim_voice.project.core.domain.repository

import org.muslim_voice.project.core.auth.GoogleAccountInfo
import org.muslim_voice.project.core.domain.model.UserProfile


import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.data.remote.dto.auth.forget_password.response.ForgetPasswordDto
import org.muslim_voice.project.core.data.remote.dto.auth.reset_password.request.ResetPasswordRequestDto
import org.muslim_voice.project.core.data.remote.dto.bases.BaseResponse
import org.muslim_voice.project.core.domain.model.auth.login.request.LoginRequestModel
import org.muslim_voice.project.core.domain.model.auth.login.response.LoginModel
import org.muslim_voice.project.core.domain.model.auth.otp.request.OtpRequestModel
import org.muslim_voice.project.core.domain.model.auth.otp.response.OtpResponseModel
import org.muslim_voice.project.core.domain.model.auth.register.request.RegisterRequestModel
import org.muslim_voice.project.core.domain.model.auth.register.response.RegisterResponseModel
import org.muslim_voice.project.core.domain.util.ApiResult

interface AuthRepository {

    fun register(
        request: RegisterRequestModel
    ): Flow<ApiResult<RegisterResponseModel>>

    fun login(
        request: LoginRequestModel
    ): Flow<ApiResult<LoginModel>>


    fun verifyOtp(
        request: OtpRequestModel
    ): Flow<ApiResult<OtpResponseModel>>


    fun forgetPassword(
        request: ForgetPasswordDto
    ): Flow<ApiResult<Unit>>

    fun resetPassword(
        request: ResetPasswordRequestDto
    ): Flow<ApiResult<ForgetPasswordDto>>



}