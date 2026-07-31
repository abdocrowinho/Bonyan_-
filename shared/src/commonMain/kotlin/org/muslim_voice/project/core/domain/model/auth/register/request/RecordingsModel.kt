package org.muslim_voice.project.core.domain.model.auth.register.request

data class RecordingsModel(
    val fajr: String? = null,
    val dhuhr: String? = null,
    val asr: String? = null,
    val maghrib: String? = null,
    val isha: String? = null
)