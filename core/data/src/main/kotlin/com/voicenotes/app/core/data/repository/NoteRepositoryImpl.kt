package com.voicenotes.app.core.data.repository

import com.voicenotes.app.core.data.audio.NoteFileStorage
import com.voicenotes.app.core.database.NoteDao
import com.voicenotes.app.core.database.toDomain
import com.voicenotes.app.core.database.toEntity
import com.voicenotes.app.core.model.VoiceNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val fileStorage: NoteFileStorage,
) : NoteRepository {

    override fun observeAllNotes(): Flow<List<VoiceNote>> =
        noteDao.observeAllNotes().map { list -> list.map { it.toDomain() } }

    override fun observeUnfiledNotes(): Flow<List<VoiceNote>> =
        noteDao.observeUnfiledNotes().map { list -> list.map { it.toDomain() } }

    override fun observeNotesInFolder(folderId: Long): Flow<List<VoiceNote>> =
        noteDao.observeNotesInFolder(folderId).map { list -> list.map { it.toDomain() } }

    override fun observeNote(noteId: Long): Flow<VoiceNote?> =
        noteDao.observeNoteById(noteId).map { it?.toDomain() }

    override suspend fun getNote(noteId: Long): VoiceNote? = noteDao.getNoteById(noteId)?.toDomain()

    override suspend fun addNote(
        title: String,
        filePath: String,
        folderId: Long?,
        durationMillis: Long,
        sizeBytes: Long,
    ): Long = noteDao.insert(
        VoiceNote(
            title = title,
            filePath = filePath,
            folderId = folderId,
            durationMillis = durationMillis,
            sizeBytes = sizeBytes,
            createdAt = Instant.now(),
        ).toEntity(),
    )

    override suspend fun renameNote(noteId: Long, title: String) = noteDao.rename(noteId, title)

    override suspend fun moveNote(noteId: Long, folderId: Long?) = noteDao.moveToFolder(noteId, folderId)

    override suspend fun deleteNote(noteId: Long) {
        val entity = noteDao.getNoteById(noteId) ?: return
        noteDao.delete(entity)
        fileStorage.deleteFile(entity.filePath)
    }
}
