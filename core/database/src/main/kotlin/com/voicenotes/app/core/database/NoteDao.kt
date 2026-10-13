package com.voicenotes.app.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun observeAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE folderId IS NULL ORDER BY createdAt DESC")
    fun observeUnfiledNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE folderId = :folderId ORDER BY createdAt DESC")
    fun observeNotesInFolder(folderId: Long): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun observeNoteById(noteId: Long): Flow<NoteEntity?>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Long): NoteEntity?

    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Query("UPDATE notes SET folderId = :folderId WHERE id = :noteId")
    suspend fun moveToFolder(noteId: Long, folderId: Long?)

    @Query("UPDATE notes SET title = :title WHERE id = :noteId")
    suspend fun rename(noteId: Long, title: String)

    @Delete
    suspend fun delete(note: NoteEntity)
}
