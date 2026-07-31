package org.muslim_voice.project.core.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.muslim_voice.project.core.constants.PrayerType
import org.muslim_voice.project.core.data.storage.LocalFileReader
import org.muslim_voice.project.core.data.storage.SupabaseStorageManager
import org.muslim_voice.project.core.domain.repository.AuthStorageRepository
import org.muslim_voice.project.core.domain.repository.UploadedAuthMedia

class AuthStorageRepositoryImpl(
    private val storageManager: SupabaseStorageManager,
) : AuthStorageRepository {
    override suspend fun uploadRegisterMedia(
        profileImageBytes: ByteArray?,
        recordingPaths: Map<PrayerType, String>,
    ): Result<UploadedAuthMedia> = runCatching {
        coroutineScope {
            val profileUpload = profileImageBytes?.let { bytes ->
                async {
                    storageManager.uploadProfilePicture(
                        bytes = bytes,
                        fileName = "Profile_Picture.jpg",
                    )
                }
            }

            val recordingUploads = recordingPaths.map { (prayerType, path) ->
                async {
                    val bytes = LocalFileReader.readBytes(path)
                        ?: error("Recording file not found: $path")
                    prayerType to storageManager.uploadAudioRecording(
                        bytes = bytes,
                        fileName = "${prayerType.name.lowercase()}.mp3",
                    )
                }
            }

            UploadedAuthMedia(
                profilePictureUrl = profileUpload?.await(),
                recordingUrls = recordingUploads.associate { it.await() },
            )
        }
    }
}
