package org.muslim_voice.project.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun createPlatformHttpClient(): HttpClient = HttpClient(Darwin)
