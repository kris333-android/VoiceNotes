package com.voicenotes.app.core.common

import javax.inject.Qualifier

/** Qualifier for the `Dispatchers.IO` coroutine dispatcher, for disk/DB/file work. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

/** Qualifier for the `Dispatchers.Default` coroutine dispatcher, for CPU-bound work. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

/** Qualifier for the `Dispatchers.Main` coroutine dispatcher. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher
