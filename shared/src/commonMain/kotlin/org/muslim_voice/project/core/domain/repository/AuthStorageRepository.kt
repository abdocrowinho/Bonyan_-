package org.muslim_voice.project.core.domain.repository

import org.muslim_voice.project.core.constants.PrayerType

data class UploadedAuthMedia(
    val profilePictureUrl: String?,
    val recordingUrls: Map<PrayerType, String>,
)

interface AuthStorageRepository {
    suspend fun uploadRegisterMedia(
        profileImageBytes: ByteArray?,
        recordingPaths: Map<PrayerType, String>,
    ): Result<UploadedAuthMedia>
}
