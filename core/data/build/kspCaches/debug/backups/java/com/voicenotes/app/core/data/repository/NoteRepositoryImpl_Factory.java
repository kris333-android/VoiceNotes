package com.voicenotes.app.core.data.repository;

import com.voicenotes.app.core.data.audio.NoteFileStorage;
import com.voicenotes.app.core.database.NoteDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class NoteRepositoryImpl_Factory implements Factory<NoteRepositoryImpl> {
  private final Provider<NoteDao> noteDaoProvider;

  private final Provider<NoteFileStorage> fileStorageProvider;

  private NoteRepositoryImpl_Factory(Provider<NoteDao> noteDaoProvider,
      Provider<NoteFileStorage> fileStorageProvider) {
    this.noteDaoProvider = noteDaoProvider;
    this.fileStorageProvider = fileStorageProvider;
  }

  @Override
  public NoteRepositoryImpl get() {
    return newInstance(noteDaoProvider.get(), fileStorageProvider.get());
  }

  public static NoteRepositoryImpl_Factory create(Provider<NoteDao> noteDaoProvider,
      Provider<NoteFileStorage> fileStorageProvider) {
    return new NoteRepositoryImpl_Factory(noteDaoProvider, fileStorageProvider);
  }

  public static NoteRepositoryImpl newInstance(NoteDao noteDao, NoteFileStorage fileStorage) {
    return new NoteRepositoryImpl(noteDao, fileStorage);
  }
}
