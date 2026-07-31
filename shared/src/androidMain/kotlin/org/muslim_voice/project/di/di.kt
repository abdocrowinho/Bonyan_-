package org.muslim_voice.project.di

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module
import org.muslim_voice.project.core.data.local.room.buildDatabase
import org.muslim_voice.project.core.media.AndroidImagePicker
import org.muslim_voice.project.core.media.ImagePicker
import org.muslim_voice.project.core.microphone.AndroidMicrophonePermissionRequester
import org.muslim_voice.project.core.microphone.AndroidRecordingManager
import org.muslim_voice.project.core.microphone.RecordingManger
import org.muslim_voice.project.core.permission.AndroidPermissionsManager
import org.muslim_voice.project.core.permission.PermissionsManager

fun androidModule(
    context: Context,
    permissionRequester: AndroidMicrophonePermissionRequester,
    permissionsManager: AndroidPermissionsManager
): Module = module {
    single<PermissionsManager> { permissionsManager }
    single<RecordingManger> { AndroidRecordingManager(context, permissionRequester) }
    single<ImagePicker>{ AndroidImagePicker(context,get()) }
    single { buildDatabase(context) }


}