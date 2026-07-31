package org.muslim_voice.project.core.data.local.room

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun buildDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath("prayer.db")
    return Room.databaseBuilder<AppDatabase>(context, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}