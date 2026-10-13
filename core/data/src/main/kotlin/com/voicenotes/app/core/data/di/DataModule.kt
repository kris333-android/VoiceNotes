package com.voicenotes.app.core.data.di

import com.voicenotes.app.core.data.repository.FolderRepository
import com.voicenotes.app.core.data.repository.FolderRepositoryImpl
import com.voicenotes.app.core.data.repository.NoteRepository
import com.voicenotes.app.core.data.repository.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Binds
    @Singleton
    abstract fun bindFolderRepository(impl: FolderRepositoryImpl): FolderRepository
}
