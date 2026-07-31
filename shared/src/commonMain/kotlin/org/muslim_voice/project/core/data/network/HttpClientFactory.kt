package org.muslim_voice.project.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.muslim_voice.project.core.logging.AppLog

expect fun createPlatformHttpClient(): HttpClient

fun createHttpClient(): HttpClient {
    val client = createPlatformHttpClient()
    return client.config {
        expectSuccess = true

        defaultRequest {
            url("https://llfnuvofwyxnyptcopup.supabase.co/functions/v1/")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true

                },
            )

        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    AppLog.d("ktor->",message) }
            }
            level = LogLevel.BODY
        }
    }
}
