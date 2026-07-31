package org.muslim_voice.project.core.domain.validation.otp

import org.muslim_voice.project.core.domain.base.BaseValidation
import org.muslim_voice.project.core.domain.base.ValidationResult
import org.muslim_voice.project.core.domain.model.auth.otp.request.OtpRequestModel
import org.muslim_voice.project.core.domain.validation.register.RegisterFieldsConstant

class OtpValidation : BaseValidation<OtpRequestModel> {
    override fun validate(param: OtpRequestModel): ValidationResult {
        val errorsMap = mutableMapOf<String, List<String>>()

        validateField(errorsMap, RegisterFieldsConstant.EMAIL, REQUIRED) {
            param.email.isNotBlank()
        }
        validateField(errorsMap, OTP_CODE, REQUIRED) {
            param.otpCode.isNotBlank()
        }

        return if (errorsMap.isEmpty()) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(errorsMap)
        }
    }

    companion object {
        const val OTP_CODE = "otpCode"
    }
}
