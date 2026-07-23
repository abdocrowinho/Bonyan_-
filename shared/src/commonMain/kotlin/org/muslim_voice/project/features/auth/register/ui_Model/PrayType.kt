package org.muslim_voice.project.features.auth.register.ui_Model

enum class PrayType {
    FAJR,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA,
}

fun PrayType.arabicLabel(): String = when (this) {
    PrayType.FAJR -> "فجر"
    PrayType.DHUHR -> "ظهر"
    PrayType.ASR -> "عصر"
    PrayType.MAGHRIB -> "مغرب"
    PrayType.ISHA -> "عشاء"
}
