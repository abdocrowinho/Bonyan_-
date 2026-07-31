package org.muslim_voice.project.core.logging

import platform.Foundation.NSLog

actual object AppLog {
    actual fun d(tag: String, message: String) {
        NSLog("DEBUG: [%s] %s", tag, message)
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        NSLog("ERROR: [%s] %s - Throwable: %s", tag, message, throwable?.message ?: "None")
    }
}