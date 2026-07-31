package org.muslim_voice.project.core.domain.validation.register

import org.muslim_voice.project.core.domain.base.BaseValidation
import org.muslim_voice.project.core.domain.base.ValidationResult
import org.muslim_voice.project.core.domain.model.auth.register.request.RegisterRequestModel

class RegisterValidation : BaseValidation<RegisterRequestModel> {

    private val GMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$", RegexOption.IGNORE_CASE)

    override fun validate(param: RegisterRequestModel): ValidationResult {
        val errorsMap = mutableMapOf<String, List<String>>()

        // 1. First Name
        validateField(errorsMap, RegisterFieldsConstant.F_NAME, REQUIRED) {
            param.firstName.isNotBlank()
        }

        // 2. Last Name
        validateField(errorsMap, RegisterFieldsConstant.L_NAME, REQUIRED) {
            param.lastName.isNotBlank()
        }

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
        validateField(
            errors = errorsMap,
            field = RegisterFieldsConstant.PASSWORD,
            message = "Password must be at least 6 characters"
        ) {
            param.password.isBlank() || param.password.length >= 6
        }

        // 5. Favorite Ayah
        validateField(errorsMap, RegisterFieldsConstant.FAV_AYAH, REQUIRED) {
            param.favoriteAyah.orEmpty().isNotBlank()
        }

        // 6. Favorite Surah
        validateField(errorsMap, RegisterFieldsConstant.FAV_SOURAH, REQUIRED) {
            param.favoriteSurah.orEmpty().isNotBlank()
        }

        // 7. Country
        validateField(errorsMap, RegisterFieldsConstant.COUNTRY, REQUIRED) {
            param.country.orEmpty().isNotBlank()
        }

        // 8. Birth Date
        validateField(errorsMap, RegisterFieldsConstant.BIRTH_DATE, REQUIRED) {
            param.birthDate.orEmpty().isNotBlank()
        }

        // 9. Role Model
        validateField(errorsMap, RegisterFieldsConstant.ROLE_MODEL, REQUIRED) {
            param.roleModel.orEmpty().isNotBlank()
        }

        return if (errorsMap.isEmpty()) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(errorsMap)
        }
    }
}