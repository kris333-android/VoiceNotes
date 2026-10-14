package com.voicenotes.app.core.data.audio

import android.content.Context
import com.voicenotes.app.core.utils.DateFormattingUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteFileStorage @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dateFormattingUtils: DateFormattingUtils
) {
    private val recordingsDir: File
        get() = File(context.filesDir, "recordings").apply { mkdirs() }

    fun createRecordingFile(): File {
        val timestamp = dateFormattingUtils.getCurrentTimestampFormatted()
        return File(recordingsDir, "note_$timestamp.m4a")
    }

    fun deleteFile(path: String) {
        runCatching { File(path).delete() }
    }
}
