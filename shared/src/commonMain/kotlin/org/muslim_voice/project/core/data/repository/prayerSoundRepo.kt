package org.muslim_voice.project.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.muslim_voice.project.core.constants.PrayerType
import org.muslim_voice.project.core.data.local.daos.PrayerSoundDao
import org.muslim_voice.project.core.data.local.entities.PrayerSoundEntity
import org.muslim_voice.project.core.microphone.RecordedAudio

class PrayerSoundRepository(private val dao: PrayerSoundDao) {
    fun observeSounds(): Flow<Map<PrayerType, RecordedAudio>> =
        dao.observeAll().map { rows ->
            rows.associate { PrayerType.valueOf(it.prayerType) to RecordedAudio(it.filePath, it.durationMillis) }
        }

    suspend fun save(prayer: PrayerType, audio: RecordedAudio) {
        dao.upsert(PrayerSoundEntity(prayer.name, audio.filePath, audio.durationMillis))
    }
}