package org.muslim_voice.project.di

import org.koin.dsl.module
import org.muslim_voice.project.core.data.local.room.buildDatabase
import org.muslim_voice.project.core.microphone.IosRecordingManager
import org.muslim_voice.project.core.microphone.RecordingManger


val iosModule = module {
    single<RecordingManger> { IosRecordingManager() }
    single { buildDatabase() }
}