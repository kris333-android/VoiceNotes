package com.voicenotes.app.core.data.repository

import com.voicenotes.app.core.model.VoiceNote
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun observeAllNotes(): Flow<List<VoiceNote>>
    fun observeUnfiledNotes(): Flow<List<VoiceNote>>
    fun observeNotesInFolder(folderId: Long): Flow<List<VoiceNote>>
    fun observeNote(noteId: Long): Flow<VoiceNote?>
    suspend fun getNote(noteId: Long): VoiceNote?
    suspend fun addNote(
        title: String,
        filePath: String,
        folderId: Long?,
        durationMillis: Long,
        sizeBytes: Long,
    ): Long
    suspend fun renameNote(noteId: Long, title: String)
    suspend fun moveNote(noteId: Long, folderId: Long?)
    suspend fun deleteNote(noteId: Long)
}
