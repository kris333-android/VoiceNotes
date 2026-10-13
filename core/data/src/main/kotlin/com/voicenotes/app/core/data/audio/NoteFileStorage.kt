package com.voicenotes.app.core.data.audio

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/** Manages where recorded audio files live on disk (app-private storage, no runtime permission needed). */
@Singleton
class NoteFileStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val recordingsDir: File
        get() = File(context.filesDir, "recordings").apply { mkdirs() }

    fun createRecordingFile(): File {
        val timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())
        return File(recordingsDir, "note_$timestamp.m4a")
    }

    fun deleteFile(path: String) {
        runCatching { File(path).delete() }
    }
}
