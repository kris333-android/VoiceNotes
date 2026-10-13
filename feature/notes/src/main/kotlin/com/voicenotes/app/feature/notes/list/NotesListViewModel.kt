package com.voicenotes.app.feature.notes.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voicenotes.app.core.data.repository.FolderRepository
import com.voicenotes.app.core.data.repository.NoteRepository
import com.voicenotes.app.core.data.share.ShareNoteHelper
import com.voicenotes.app.core.model.ALL_NOTES_FOLDER_ID
import com.voicenotes.app.core.model.Folder
import com.voicenotes.app.core.model.UNFILED_FOLDER_ID
import com.voicenotes.app.core.model.VoiceNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotesListUiState(
    val title: String = "",
    val notes: List<VoiceNote> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val isLoading: Boolean = true,
    val noteRecordFolderId: Long? = null,
)

@HiltViewModel
class NotesListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
    private val shareNoteHelper: ShareNoteHelper,
) : ViewModel() {

    private val folderId: Long = checkNotNull(savedStateHandle["folderId"])

    private val recordFolderId: Long? = folderId.takeIf { it != ALL_NOTES_FOLDER_ID && it != UNFILED_FOLDER_ID }

    private val notesFlow = when (folderId) {
        ALL_NOTES_FOLDER_ID -> noteRepository.observeAllNotes()
        UNFILED_FOLDER_ID -> noteRepository.observeUnfiledNotes()
        else -> noteRepository.observeNotesInFolder(folderId)
    }

    private val foldersFlow = folderRepository.observeFolders().map { list -> list.map { it.folder } }

    private val titleState = MutableStateFlow(defaultTitle())

    val uiState: StateFlow<NotesListUiState> = combine(
        notesFlow,
        foldersFlow,
        titleState,
    ) { notes, folders, title ->
        NotesListUiState(
            title = title,
            notes = notes,
            folders = folders,
            isLoading = false,
            noteRecordFolderId = recordFolderId,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NotesListUiState(title = defaultTitle()))

    init {
        if (recordFolderId != null) {
            viewModelScope.launch {
                folderRepository.getFolder(recordFolderId)?.let { folder -> titleState.value = folder.name }
            }
        }
    }

    fun renameNote(noteId: Long, newTitle: String) {
        viewModelScope.launch { noteRepository.renameNote(noteId, newTitle) }
    }

    fun moveNote(noteId: Long, targetFolderId: Long?) {
        viewModelScope.launch { noteRepository.moveNote(noteId, targetFolderId) }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch { noteRepository.deleteNote(noteId) }
    }

    fun createFolderAndMoveNote(noteId: Long, folderName: String) {
        viewModelScope.launch {
            val newFolderId = folderRepository.createFolder(folderName)
            noteRepository.moveNote(noteId, newFolderId)
        }
    }

    fun shareNote(note: VoiceNote) = shareNoteHelper.share(note)

    private fun defaultTitle(): String = when (folderId) {
        ALL_NOTES_FOLDER_ID -> "All notes"
        UNFILED_FOLDER_ID -> "Unfiled"
        else -> ""
    }
}
