package com.voicenotes.app.core.database.di

import android.content.Context
import androidx.room.Room
import com.voicenotes.app.core.database.FolderDao
import com.voicenotes.app.core.database.NoteDao
import com.voicenotes.app.core.database.VoiceNotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideVoiceNotesDatabase(@ApplicationContext context: Context): VoiceNotesDatabase =
        Room.databaseBuilder(
            context,
            VoiceNotesDatabase::class.java,
            "voice_notes.db",
        ).build()

    @Provides
    fun provideNoteDao(database: VoiceNotesDatabase): NoteDao = database.noteDao()

    @Provides
    fun provideFolderDao(database: VoiceNotesDatabase): FolderDao = database.folderDao()
}
