package com.voicenotes.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voicenotes.app.core.data.preferences.UserPreferencesRepository
import com.voicenotes.app.core.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = userPreferencesRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeMode.SYSTEM)
}
