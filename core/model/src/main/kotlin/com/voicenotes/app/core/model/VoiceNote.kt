package com.voicenotes.app.core.model

import java.time.Instant

/** A single recorded voice note. */
data class VoiceNote(
    val id: Long = 0L,
    val title: String,
    val filePath: String,
    val folderId: Long?,
    val durationMillis: Long,
    val sizeBytes: Long,
    val createdAt: Instant,
)
