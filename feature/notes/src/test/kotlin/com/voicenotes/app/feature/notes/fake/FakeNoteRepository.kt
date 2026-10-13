package com.voicenotes.app.feature.notes.fake

import com.voicenotes.app.core.data.repository.NoteRepository
import com.voicenotes.app.core.model.VoiceNote
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** In-memory [NoteRepository] for ViewModel unit tests. */
class FakeNoteRepository : NoteRepository {

    private val notesFlow = MutableStateFlow<List<VoiceNote>>(emptyList())
    private var nextId = 1L

    /** Seeds the fake with a note, assigning it an id, and returns that id. */
    fun addTestNote(
        title: String = "Test note",
        filePath: String = "/tmp/test.m4a",
        folderId: Long? = null,
        durationMillis: Long = 1_000L,
        sizeBytes: Long = 100L,
    ): Long {
        val id = nextId++
        notesFlow.update {
            it + VoiceNote(
                id = id,
                title = title,
                filePath = filePath,
                folderId = folderId,
                durationMillis = durationMillis,
                sizeBytes = sizeBytes,
                createdAt = Instant.now(),
            )
        }
        return id
    }

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
    ): Long = addTestNote(title, filePath, folderId, durationMillis, sizeBytes)

    override suspend fun renameNote(noteId: Long, title: String) {
        notesFlow.update { list -> list.map { if (it.id == noteId) it.copy(title = title) else it } }
    }

    override suspend fun moveNote(noteId: Long, folderId: Long?) {
        notesFlow.update { list -> list.map { if (it.id == noteId) it.copy(folderId = folderId) else it } }
    }

    override suspend fun deleteNote(noteId: Long) {
        notesFlow.update { list -> list.filterNot { it.id == noteId } }
    }
}
