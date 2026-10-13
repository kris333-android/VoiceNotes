package com.voicenotes.app.feature.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voicenotes.app.core.data.preferences.UserPreferencesRepository
import com.voicenotes.app.core.data.repository.FolderRepository
import com.voicenotes.app.core.model.FolderWithNoteCount
import com.voicenotes.app.core.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FoldersUiState(
    val allNotesCount: Int = 0,
    val unfiledCount: Int = 0,
    val folders: List<FolderWithNoteCount> = emptyList(),
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isLoading: Boolean = true,
)

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val folderRepository: FolderRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    val uiState: StateFlow<FoldersUiState> = combine(
        folderRepository.observeFolders(),
        folderRepository.observeAllNotesCount(),
        folderRepository.observeUnfiledNotesCount(),
        userPreferencesRepository.themeMode,
    ) { folders, allCount, unfiledCount, themeMode ->
        FoldersUiState(
            allNotesCount = allCount,
            unfiledCount = unfiledCount,
            folders = folders,
            themeMode = themeMode,
            isLoading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FoldersUiState())

    fun createFolder(name: String) {
        viewModelScope.launch { folderRepository.createFolder(name) }
    }

    fun renameFolder(folderId: Long, name: String) {
        viewModelScope.launch { folderRepository.renameFolder(folderId, name) }
    }

    fun deleteFolder(folderId: Long) {
        viewModelScope.launch { folderRepository.deleteFolder(folderId) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { userPreferencesRepository.setThemeMode(mode) }
    }
}
