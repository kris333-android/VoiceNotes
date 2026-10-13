package com.voicenotes.app.feature.record;

import androidx.lifecycle.SavedStateHandle;
import com.voicenotes.app.core.data.audio.AudioRecorderController;
import com.voicenotes.app.core.data.audio.NoteFileStorage;
import com.voicenotes.app.core.data.repository.NoteRepository;
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
public final class RecordViewModel_Factory implements Factory<RecordViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<AudioRecorderController> recorderControllerProvider;

  private final Provider<NoteRepository> noteRepositoryProvider;

  private final Provider<NoteFileStorage> fileStorageProvider;

  private RecordViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AudioRecorderController> recorderControllerProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<NoteFileStorage> fileStorageProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.recorderControllerProvider = recorderControllerProvider;
    this.noteRepositoryProvider = noteRepositoryProvider;
    this.fileStorageProvider = fileStorageProvider;
  }

  @Override
  public RecordViewModel get() {
    return newInstance(savedStateHandleProvider.get(), recorderControllerProvider.get(), noteRepositoryProvider.get(), fileStorageProvider.get());
  }

  public static RecordViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AudioRecorderController> recorderControllerProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<NoteFileStorage> fileStorageProvider) {
    return new RecordViewModel_Factory(savedStateHandleProvider, recorderControllerProvider, noteRepositoryProvider, fileStorageProvider);
  }

  public static RecordViewModel newInstance(SavedStateHandle savedStateHandle,
      AudioRecorderController recorderController, NoteRepository noteRepository,
      NoteFileStorage fileStorage) {
    return new RecordViewModel(savedStateHandle, recorderController, noteRepository, fileStorage);
  }
}
