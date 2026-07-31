package org.muslim_voice.project.features.auth.register.ui_Model

import org.muslim_voice.project.core.constants.PrayerType


fun PrayerType .arabicLabel(): String = when (this) {
    PrayerType.FAJR -> "فجر"
    PrayerType.DHUHR -> "ظهر"
    PrayerType.ASR -> "عصر"
    PrayerType.MAGHRIB -> "مغرب"
    PrayerType.ISHA -> "عشاء"
}
