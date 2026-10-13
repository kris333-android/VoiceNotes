package com.voicenotes.app.core.data.share;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class ShareNoteHelper_Factory implements Factory<ShareNoteHelper> {
  private final Provider<Context> contextProvider;

  private ShareNoteHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ShareNoteHelper get() {
    return newInstance(contextProvider.get());
  }

  public static ShareNoteHelper_Factory create(Provider<Context> contextProvider) {
    return new ShareNoteHelper_Factory(contextProvider);
  }

  public static ShareNoteHelper newInstance(Context context) {
    return new ShareNoteHelper(context);
  }
}
