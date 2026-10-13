package com.voicenotes.app.feature.folders.fake

import com.voicenotes.app.core.data.repository.FolderRepository
import com.voicenotes.app.core.model.Folder
import com.voicenotes.app.core.model.FolderWithNoteCount
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** In-memory [FolderRepository] for ViewModel unit tests. */
class FakeFolderRepository : FolderRepository {

    private val foldersFlow = MutableStateFlow<List<Folder>>(emptyList())
    private var nextId = 1L

    fun currentFolders(): List<Folder> = foldersFlow.value

    fun seedFolder(name: String): Long {
        val id = nextId++
        foldersFlow.value = foldersFlow.value + Folder(id = id, name = name, createdAt = Instant.now())
        return id
    }

    override fun observeFolders(): Flow<List<FolderWithNoteCount>> =
        foldersFlow.map { list -> list.map { FolderWithNoteCount(it, noteCount = 0) } }

    override fun observeAllNotesCount(): Flow<Int> = MutableStateFlow(0)

    override fun observeUnfiledNotesCount(): Flow<Int> = MutableStateFlow(0)

    override suspend fun getFolder(folderId: Long): Folder? = foldersFlow.value.find { it.id == folderId }

    override suspend fun createFolder(name: String): Long {
        val id = nextId++
        foldersFlow.value = foldersFlow.value + Folder(id = id, name = name, createdAt = Instant.now())
        return id
    }

    override suspend fun renameFolder(folderId: Long, name: String) {
        foldersFlow.value = foldersFlow.value.map { if (it.id == folderId) it.copy(name = name) else it }
    }

    override suspend fun deleteFolder(folderId: Long) {
        foldersFlow.value = foldersFlow.value.filterNot { it.id == folderId }
    }
}
