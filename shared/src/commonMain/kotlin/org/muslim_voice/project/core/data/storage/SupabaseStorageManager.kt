package org.muslim_voice.project.core.data.storage

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.encodeURLPathPart
import io.ktor.http.isSuccess
import kotlinx.datetime.Clock

class SupabaseStorageManager(
    private val httpClient: HttpClient,
) {
    suspend fun uploadProfilePicture(bytes: ByteArray, fileName: String): String {
        return uploadBytes(
            bytes = bytes,
            bucket = SupabaseStorageConfig.PROFILE_PICTURE_BUCKET,
            fileName = fileName,
            contentType = ContentType.Image.JPEG,
        )
    }

    suspend fun uploadAudioRecording(bytes: ByteArray, fileName: String): String {
        return uploadBytes(
            bytes = bytes,
            bucket = SupabaseStorageConfig.RECORDINGS_BUCKET,
            fileName = fileName,
            contentType = ContentType.Audio.MPEG,
        )
    }

    private suspend fun uploadBytes(
        bytes: ByteArray,
        bucket: String,
        fileName: String,
        contentType: ContentType,
    ): String {
        val uniqueFileName = uniqueFileName(fileName)
        val encodedBucket = bucket.encodeURLPathPart()
        val encodedFileName = uniqueFileName.encodeURLPathPart()
        val uploadUrl = "${SupabaseStorageConfig.SUPABASE_URL}/storage/v1/object/$encodedBucket/$encodedFileName"

        val response = httpClient.post(uploadUrl) {
            header("apikey", SupabaseStorageConfig.SUPABASE_PUBLISHABLE_KEY)
            header(HttpHeaders.Authorization, "Bearer ${SupabaseStorageConfig.SUPABASE_PUBLISHABLE_KEY}")
            header(HttpHeaders.ContentType, contentType)
            header("x-upsert", "false")
            setBody(bytes)
        }

        if (!response.status.isSuccess()) {
            error("Supabase upload failed (${response.status.value}): ${response.bodyAsText()}")
        }

        return "${SupabaseStorageConfig.SUPABASE_URL}/storage/v1/object/public/$encodedBucket/$encodedFileName"
    }

    private fun uniqueFileName(fileName: String): String {
        val safeName = fileName
            .substringAfterLast('/')
            .substringAfterLast('\\')
            .replace(Regex("[^A-Za-z0-9._-]"), "_")
            .ifBlank { "upload" }
        return "${Clock.System.now().toEpochMilliseconds()}_$safeName"
    }
}
