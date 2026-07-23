package org.muslim_voice.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform