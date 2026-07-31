package org.muslim_voice.project.core.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.muslim_voice.project.core.data.remote.dto.auth.forget_password.response.ForgetPasswordDto
import org.muslim_voice.project.core.data.remote.dto.auth.login.request.LoginRequestDto
import org.muslim_voice.project.core.data.remote.dto.auth.login.response.LoginDataDto
import org.muslim_voice.project.core.data.remote.dto.auth.otp.request.OtpRequestDto
import org.muslim_voice.project.core.data.remote.dto.auth.otp.response.OtpResponseDto
import org.muslim_voice.project.core.data.remote.dto.auth.register.request.RegisterRequestDto
import org.muslim_voice.project.core.data.remote.dto.auth.register.response.RegisterResponseDto
import org.muslim_voice.project.core.data.remote.dto.auth.reset_password.request.ResetPasswordRequestDto
import org.muslim_voice.project.core.data.remote.dto.bases.BaseResponse
import org.muslim_voice.project.core.data.remote.webservice.MainWebServices_CORE.auth.AuthApiConstant
import org.muslim_voice.project.core.logging.AppLog

class AuthApiService(
    private val httpClient: HttpClient
) {

    suspend fun register(
        request: RegisterRequestDto
    ): RegisterResponseDto {
        return httpClient.post(AuthApiConstant.REGISTER) {
            setBody(request)
        }.body()
    }


    suspend fun login(request: LoginRequestDto): BaseResponse<LoginDataDto> {

        AppLog.d("gpt", "before post")

        val response = httpClient.post(AuthApiConstant.LOGIN) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        AppLog.d("gpt", "after post")

        AppLog.d("gpt", response.status.toString())

        AppLog.d("gpt", "before body")

        val body = response.body<BaseResponse<LoginDataDto>>()

        AppLog.d("gpt", "after body")

        return body
    }

    suspend fun verifyOtp(
        request: OtpRequestDto
    ): BaseResponse<OtpResponseDto> {
        return httpClient.post(AuthApiConstant.OTP_VERIFICATION) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }


    suspend fun forgetPassword(
        request: ForgetPasswordDto
    ): BaseResponse<ForgetPasswordDto> {
        return httpClient.post(AuthApiConstant.FORGET_PASSWORD) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    suspend fun resetPassword(
        request: ResetPasswordRequestDto
    ): BaseResponse<ForgetPasswordDto> {
        return httpClient.post(AuthApiConstant.RESET_PASSWORD) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}