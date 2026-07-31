package org.muslim_voice.project.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_sounds")
data class PrayerSoundEntity(
    @PrimaryKey val prayerType: String,
    val filePath: String,
    val durationMillis: Long
)