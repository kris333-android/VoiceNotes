package com.voicenotes.app.feature.folders;

import com.voicenotes.app.core.data.preferences.UserPreferencesRepository;
import com.voicenotes.app.core.data.repository.FolderRepository;
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
public final class FoldersViewModel_Factory implements Factory<FoldersViewModel> {
  private final Provider<FolderRepository> folderRepositoryProvider;

  private final Provider<UserPreferencesRepository> userPreferencesRepositoryProvider;

  private FoldersViewModel_Factory(Provider<FolderRepository> folderRepositoryProvider,
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider) {
    this.folderRepositoryProvider = folderRepositoryProvider;
    this.userPreferencesRepositoryProvider = userPreferencesRepositoryProvider;
  }

  @Override
  public FoldersViewModel get() {
    return newInstance(folderRepositoryProvider.get(), userPreferencesRepositoryProvider.get());
  }

  public static FoldersViewModel_Factory create(Provider<FolderRepository> folderRepositoryProvider,
      Provider<UserPreferencesRepository> userPreferencesRepositoryProvider) {
    return new FoldersViewModel_Factory(folderRepositoryProvider, userPreferencesRepositoryProvider);
  }

  public static FoldersViewModel newInstance(FolderRepository folderRepository,
      UserPreferencesRepository userPreferencesRepository) {
    return new FoldersViewModel(folderRepository, userPreferencesRepository);
  }
}
