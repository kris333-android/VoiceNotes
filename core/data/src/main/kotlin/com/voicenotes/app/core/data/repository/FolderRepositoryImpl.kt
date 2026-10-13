package com.voicenotes.app.core.data.repository

import com.voicenotes.app.core.database.FolderDao
import com.voicenotes.app.core.database.toDomain
import com.voicenotes.app.core.database.toEntity
import com.voicenotes.app.core.model.Folder
import com.voicenotes.app.core.model.FolderWithNoteCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao,
) : FolderRepository {

    override fun observeFolders(): Flow<List<FolderWithNoteCount>> =
        folderDao.observeFoldersWithCounts().map { rows ->
            rows.map { row -> FolderWithNoteCount(row.folder.toDomain(), row.noteCount) }
        }

    override fun observeAllNotesCount(): Flow<Int> = folderDao.observeAllNotesCount()

    override fun observeUnfiledNotesCount(): Flow<Int> = folderDao.observeUnfiledNotesCount()

    override suspend fun getFolder(folderId: Long): Folder? =
        folderDao.getFolderById(folderId)?.toDomain()

    override suspend fun createFolder(name: String): Long =
        folderDao.insert(Folder(name = name, createdAt = Instant.now()).toEntity())

    override suspend fun renameFolder(folderId: Long, name: String) {
        val existing = folderDao.getFolderById(folderId) ?: return
        folderDao.update(existing.copy(name = name))
    }

    override suspend fun deleteFolder(folderId: Long) {
        val existing = folderDao.getFolderById(folderId) ?: return
        folderDao.delete(existing)
    }
}
