package com.voicenotes.app.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.voicenotes.app.core.model.Folder
import java.time.Instant

@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val createdAt: Long,
)

fun FolderEntity.toDomain(): Folder = Folder(
    id = id,
    name = name,
    createdAt = Instant.ofEpochMilli(createdAt),
)

fun Folder.toEntity(): FolderEntity = FolderEntity(
    id = id,
    name = name,
    createdAt = createdAt.toEpochMilli(),
)
