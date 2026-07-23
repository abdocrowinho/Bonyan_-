package org.muslim_voice.project.core.mvi

import org.muslim_voice.project.core.data.helper.AppError

interface ConnectionAwareState {
    val isOffline: Boolean
    val appError: AppError?
}

fun ConnectionAwareState.shouldShowOfflineBanner(): Boolean =
    isOffline || appError == AppError.Connection
