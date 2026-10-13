package com.voicenotes.app.core.data.repository

import com.voicenotes.app.core.model.Folder
import com.voicenotes.app.core.model.FolderWithNoteCount
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    fun observeFolders(): Flow<List<FolderWithNoteCount>>
    fun observeAllNotesCount(): Flow<Int>
    fun observeUnfiledNotesCount(): Flow<Int>
    suspend fun getFolder(folderId: Long): Folder?
    suspend fun createFolder(name: String): Long
    suspend fun renameFolder(folderId: Long, name: String)
    suspend fun deleteFolder(folderId: Long)
}
