package org.muslim_voice.project.core.logging

// androidMain
import android.util.Log

actual object AppLog {
    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}