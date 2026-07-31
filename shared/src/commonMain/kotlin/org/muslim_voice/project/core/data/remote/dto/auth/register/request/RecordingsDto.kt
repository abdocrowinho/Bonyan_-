package org.muslim_voice.project.core.data.remote.dto.auth.register.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordingsDto(
    @SerialName("fajr")
    val fajr: String? = null,

    @SerialName("dhuhr")
    val dhuhr: String? = null,

    @SerialName("asr")
    val asr: String? = null,

    @SerialName("maghrib")
    val maghrib: String? = null,

    @SerialName("isha")
    val isha: String? = null
)