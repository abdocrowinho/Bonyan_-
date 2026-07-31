package org.muslim_voice.project.core.domain.base


import kotlin.collections.set

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Failure(val errors: Map<String, List<String> >) : ValidationResult()
}

interface BaseValidation<T> {
    val REQUIRED get() = "field is required"
    val INVALID_NUM get() = "Invalid number"
    val INVALID_EMAIL get() = "email not valid"

    fun validate(param: T): ValidationResult

    fun validateField(
        errors: MutableMap<String, List<String>>,
        field: String,
        message: String,
        condition: () -> Boolean
    ) {
        if (!condition()) {
            errors[field] = (errors[field] ?: emptyList()) + message
        }
    }
}