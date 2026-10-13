package com.voicenotes.app.feature.notes.list;

import androidx.lifecycle.SavedStateHandle;
import com.voicenotes.app.core.data.repository.FolderRepository;
import com.voicenotes.app.core.data.repository.NoteRepository;
import com.voicenotes.app.core.data.share.ShareNoteHelper;
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
public final class NotesListViewModel_Factory implements Factory<NotesListViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<NoteRepository> noteRepositoryProvider;

  private final Provider<FolderRepository> folderRepositoryProvider;

  private final Provider<ShareNoteHelper> shareNoteHelperProvider;

  private NotesListViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<FolderRepository> folderRepositoryProvider,
      Provider<ShareNoteHelper> shareNoteHelperProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.noteRepositoryProvider = noteRepositoryProvider;
    this.folderRepositoryProvider = folderRepositoryProvider;
    this.shareNoteHelperProvider = shareNoteHelperProvider;
  }

  @Override
  public NotesListViewModel get() {
    return newInstance(savedStateHandleProvider.get(), noteRepositoryProvider.get(), folderRepositoryProvider.get(), shareNoteHelperProvider.get());
  }

  public static NotesListViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<FolderRepository> folderRepositoryProvider,
      Provider<ShareNoteHelper> shareNoteHelperProvider) {
    return new NotesListViewModel_Factory(savedStateHandleProvider, noteRepositoryProvider, folderRepositoryProvider, shareNoteHelperProvider);
  }

  public static NotesListViewModel newInstance(SavedStateHandle savedStateHandle,
      NoteRepository noteRepository, FolderRepository folderRepository,
      ShareNoteHelper shareNoteHelper) {
    return new NotesListViewModel(savedStateHandle, noteRepository, folderRepository, shareNoteHelper);
  }
}
