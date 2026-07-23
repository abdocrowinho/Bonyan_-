package org.muslim_voice.project.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual fun createPlatformHttpClient(): HttpClient = HttpClient(CIO)
