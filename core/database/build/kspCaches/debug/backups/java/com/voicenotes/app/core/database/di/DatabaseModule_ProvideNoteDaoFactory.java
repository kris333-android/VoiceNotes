package com.voicenotes.app.core.database.di;

import com.voicenotes.app.core.database.NoteDao;
import com.voicenotes.app.core.database.VoiceNotesDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideNoteDaoFactory implements Factory<NoteDao> {
  private final Provider<VoiceNotesDatabase> databaseProvider;

  private DatabaseModule_ProvideNoteDaoFactory(Provider<VoiceNotesDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public NoteDao get() {
    return provideNoteDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideNoteDaoFactory create(
      Provider<VoiceNotesDatabase> databaseProvider) {
    return new DatabaseModule_ProvideNoteDaoFactory(databaseProvider);
  }

  public static NoteDao provideNoteDao(VoiceNotesDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideNoteDao(database));
  }
}
