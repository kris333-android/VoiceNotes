package com.voicenotes.app.feature.notes.detail;

import androidx.lifecycle.SavedStateHandle;
import com.voicenotes.app.core.data.audio.AudioPlayerController;
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
public final class NoteDetailViewModel_Factory implements Factory<NoteDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<NoteRepository> noteRepositoryProvider;

  private final Provider<FolderRepository> folderRepositoryProvider;

  private final Provider<AudioPlayerController> playerControllerProvider;

  private final Provider<ShareNoteHelper> shareNoteHelperProvider;

  private NoteDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<FolderRepository> folderRepositoryProvider,
      Provider<AudioPlayerController> playerControllerProvider,
      Provider<ShareNoteHelper> shareNoteHelperProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.noteRepositoryProvider = noteRepositoryProvider;
    this.folderRepositoryProvider = folderRepositoryProvider;
    this.playerControllerProvider = playerControllerProvider;
    this.shareNoteHelperProvider = shareNoteHelperProvider;
  }

  @Override
  public NoteDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), noteRepositoryProvider.get(), folderRepositoryProvider.get(), playerControllerProvider.get(), shareNoteHelperProvider.get());
  }

  public static NoteDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<NoteRepository> noteRepositoryProvider,
      Provider<FolderRepository> folderRepositoryProvider,
      Provider<AudioPlayerController> playerControllerProvider,
      Provider<ShareNoteHelper> shareNoteHelperProvider) {
    return new NoteDetailViewModel_Factory(savedStateHandleProvider, noteRepositoryProvider, folderRepositoryProvider, playerControllerProvider, shareNoteHelperProvider);
  }

  public static NoteDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      NoteRepository noteRepository, FolderRepository folderRepository,
      AudioPlayerController playerController, ShareNoteHelper shareNoteHelper) {
    return new NoteDetailViewModel(savedStateHandle, noteRepository, folderRepository, playerController, shareNoteHelper);
  }
}
