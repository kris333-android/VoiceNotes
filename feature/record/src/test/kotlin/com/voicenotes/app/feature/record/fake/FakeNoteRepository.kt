package com.voicenotes.app.feature.record.fake

import com.voicenotes.app.core.data.repository.NoteRepository
import com.voicenotes.app.core.model.VoiceNote
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** In-memory [NoteRepository] for ViewModel unit tests. */
class FakeNoteRepository : NoteRepository {

    private val notesFlow = MutableStateFlow<List<VoiceNote>>(emptyList())
    private var nextId = 1L

    fun allNotes(): List<VoiceNote> = notesFlow.value

    override fun observeAllNotes(): Flow<List<VoiceNote>> = notesFlow

    override fun observeUnfiledNotes(): Flow<List<VoiceNote>> =
        notesFlow.map { list -> list.filter { it.folderId == null } }

    override fun observeNotesInFolder(folderId: Long): Flow<List<VoiceNote>> =
        notesFlow.map { list -> list.filter { it.folderId == folderId } }

    override fun observeNote(noteId: Long): Flow<VoiceNote?> =
        notesFlow.map { list -> list.find { it.id == noteId } }

    override suspend fun getNote(noteId: Long): VoiceNote? = notesFlow.value.find { it.id == noteId }

    override suspend fun addNote(
        title: String,
        filePath: String,
        folderId: Long?,
        durationMillis: Long,
        sizeBytes: Long,
    ): Long {
        val id = nextId++
        notesFlow.value = notesFlow.value + VoiceNote(
            id = id,
            title = title,
            filePath = filePath,
            folderId = folderId,
            durationMillis = durationMillis,
            sizeBytes = sizeBytes,
            createdAt = Instant.now(),
        )
        return id
    }

    override suspend fun renameNote(noteId: Long, title: String) {
        notesFlow.value = notesFlow.value.map { if (it.id == noteId) it.copy(title = title) else it }
    }

    override suspend fun moveNote(noteId: Long, folderId: Long?) {
        notesFlow.value = notesFlow.value.map { if (it.id == noteId) it.copy(folderId = folderId) else it }
    }

    override suspend fun deleteNote(noteId: Long) {
        notesFlow.value = notesFlow.value.filterNot { it.id == noteId }
    }
}
