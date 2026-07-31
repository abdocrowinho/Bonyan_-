package org.muslim_voice.project.core.data.storage

expect object LocalFileReader {
    suspend fun readBytes(path: String): ByteArray?
}
