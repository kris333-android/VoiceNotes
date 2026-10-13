package com.voicenotes.app.core.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.voicenotes.app.core.model.VoiceNote
import java.time.Instant

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("folderId")],
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val filePath: String,
    val folderId: Long?,
    val durationMillis: Long,
    val sizeBytes: Long,
    val createdAt: Long,
)

fun NoteEntity.toDomain(): VoiceNote = VoiceNote(
    id = id,
    title = title,
    filePath = filePath,
    folderId = folderId,
    durationMillis = durationMillis,
    sizeBytes = sizeBytes,
    createdAt = Instant.ofEpochMilli(createdAt),
)

fun VoiceNote.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    filePath = filePath,
    folderId = folderId,
    durationMillis = durationMillis,
    sizeBytes = sizeBytes,
    createdAt = createdAt.toEpochMilli(),
)
