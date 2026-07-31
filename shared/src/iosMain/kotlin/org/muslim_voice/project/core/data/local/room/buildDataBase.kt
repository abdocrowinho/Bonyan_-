package org.muslim_voice.project.core.data.local.room

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

fun buildDatabase(): AppDatabase {
    val documentsPath = NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory, NSUserDomainMask, true
    ).first() as String
    return Room.databaseBuilder<AppDatabase>("$documentsPath/prayer.db")
        .setDriver(BundledSQLiteDriver())
        .build()
}