package com.voicenotes.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoteEntity::class, FolderEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class VoiceNotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
}
