package com.voicenotes.app.core.database.di;

import android.content.Context;
import com.voicenotes.app.core.database.VoiceNotesDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideVoiceNotesDatabaseFactory implements Factory<VoiceNotesDatabase> {
  private final Provider<Context> contextProvider;

  private DatabaseModule_ProvideVoiceNotesDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VoiceNotesDatabase get() {
    return provideVoiceNotesDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideVoiceNotesDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideVoiceNotesDatabaseFactory(contextProvider);
  }

  public static VoiceNotesDatabase provideVoiceNotesDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideVoiceNotesDatabase(context));
  }
}
