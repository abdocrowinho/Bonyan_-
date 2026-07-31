package org.muslim_voice.project.core.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.muslim_voice.project.core.data.local.entities.PrayerSoundEntity

@Dao
interface PrayerSoundDao {
    @Upsert
    suspend fun upsert(sound: PrayerSoundEntity)

    @Query("SELECT * FROM prayer_sounds")
    fun observeAll(): Flow<List<PrayerSoundEntity>>
}