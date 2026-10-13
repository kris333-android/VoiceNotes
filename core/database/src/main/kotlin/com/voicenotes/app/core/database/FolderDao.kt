package com.voicenotes.app.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class FolderWithCountRow(
    @Embedded val folder: FolderEntity,
    val noteCount: Int,
)

@Dao
interface FolderDao {

    @Query(
        """
        SELECT folders.*, COUNT(notes.id) AS noteCount
        FROM folders
        LEFT JOIN notes ON notes.folderId = folders.id
        GROUP BY folders.id
        ORDER BY folders.createdAt DESC
        """,
    )
    fun observeFoldersWithCounts(): Flow<List<FolderWithCountRow>>

    @Query("SELECT * FROM folders WHERE id = :folderId")
    suspend fun getFolderById(folderId: Long): FolderEntity?

    @Query("SELECT COUNT(*) FROM notes")
    fun observeAllNotesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM notes WHERE folderId IS NULL")
    fun observeUnfiledNotesCount(): Flow<Int>

    @Insert
    suspend fun insert(folder: FolderEntity): Long

    @Update
    suspend fun update(folder: FolderEntity)

    @Delete
    suspend fun delete(folder: FolderEntity)
}
