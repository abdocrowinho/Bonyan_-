package org.muslim_voice.project.core.domain.validation.login

import org.muslim_voice.project.core.domain.base.BaseValidation
import org.muslim_voice.project.core.domain.base.ValidationResult
import org.muslim_voice.project.core.domain.model.auth.login.request.LoginRequestModel
import org.muslim_voice.project.core.domain.validation.register.RegisterFieldsConstant

class LoginValidation : BaseValidation<LoginRequestModel> {
    private val GMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$", RegexOption.IGNORE_CASE)

    override fun validate(param: LoginRequestModel): ValidationResult {
        val errorsMap = mutableMapOf<String, List<String>>()

        // 3. Email
        validateField(errorsMap, RegisterFieldsConstant.EMAIL, REQUIRED) {
            param.email.isNotBlank()
        }
        validateField(errorsMap, RegisterFieldsConstant.EMAIL, INVALID_EMAIL) {
            param.email.isBlank() || param.email.matches(GMAIL_REGEX)
        }

        // 4. Password
        validateField(errorsMap, RegisterFieldsConstant.PASSWORD, REQUIRED) {
            param.password.isNotBlank()
        }
        return if (errorsMap.isEmpty()) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(errorsMap)
        }
    }
}