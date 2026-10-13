package com.voicenotes.app.core.data.audio;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class NoteFileStorage_Factory implements Factory<NoteFileStorage> {
  private final Provider<Context> contextProvider;

  private NoteFileStorage_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public NoteFileStorage get() {
    return newInstance(contextProvider.get());
  }

  public static NoteFileStorage_Factory create(Provider<Context> contextProvider) {
    return new NoteFileStorage_Factory(contextProvider);
  }

  public static NoteFileStorage newInstance(Context context) {
    return new NoteFileStorage(context);
  }
}
