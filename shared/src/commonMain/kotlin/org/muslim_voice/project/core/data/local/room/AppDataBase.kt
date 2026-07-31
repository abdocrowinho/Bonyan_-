package org.muslim_voice.project.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.muslim_voice.project.core.data.local.daos.PrayerSoundDao
import org.muslim_voice.project.core.data.local.entities.PrayerSoundEntity

@Database(entities = [PrayerSoundEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerSoundDao(): PrayerSoundDao
}