package org.muslim_voice.project.core.domain.model.auth.register.response

data class RegisterResponseModel(
    val email: String,
    val resendId: String,
    val message: String
)