package com.voicenotes.app.feature.notes.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voicenotes.app.core.data.audio.AudioPlayerController
import com.voicenotes.app.core.data.repository.FolderRepository
import com.voicenotes.app.core.data.repository.NoteRepository
import com.voicenotes.app.core.data.share.ShareNoteHelper
import com.voicenotes.app.core.model.Folder
import com.voicenotes.app.core.model.VoiceNote
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NoteDetailUiState(
    val note: VoiceNote? = null,
    val folders: List<Folder> = emptyList(),
    val playerState: AudioPlayerController.State = AudioPlayerController.State.Idle,
    val isDeleted: Boolean = false,
)

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
    private val playerController: AudioPlayerController,
    private val shareNoteHelper: ShareNoteHelper,
) : ViewModel() {

    private val noteId: Long = checkNotNull(savedStateHandle["noteId"])

    private val isDeletedState = MutableStateFlow(false)
    private var loadedFilePath: String? = null

    private val noteFlow = noteRepository.observeNote(noteId)
    private val foldersFlow = folderRepository.observeFolders().map { list -> list.map { it.folder } }

    val uiState: StateFlow<NoteDetailUiState> = combine(
        noteFlow,
        foldersFlow,
        playerController.state,
        isDeletedState,
    ) { note, folders, playerState, isDeleted ->
        if (note != null && loadedFilePath != note.filePath) {
            loadedFilePath = note.filePath
            playerController.load(File(note.filePath))
        }
        NoteDetailUiState(note = note, folders = folders, playerState = playerState, isDeleted = isDeleted)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NoteDetailUiState())

    fun togglePlayback() {
        when (playerController.state.value) {
            is AudioPlayerController.State.Playing -> playerController.pause()
            else -> playerController.play()
        }
    }

    fun seekTo(positionMillis: Long) = playerController.seekTo(positionMillis)

    fun rename(newTitle: String) {
        viewModelScope.launch { noteRepository.renameNote(noteId, newTitle) }
    }

    fun move(folderId: Long?) {
        viewModelScope.launch { noteRepository.moveNote(noteId, folderId) }
    }

    fun createFolderAndMoveNote(folderName: String) {
        viewModelScope.launch {
            val newFolderId = folderRepository.createFolder(folderName)
            noteRepository.moveNote(noteId, newFolderId)
        }
    }

    fun delete() {
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
            isDeletedState.value = true
        }
    }

    fun share() {
        uiState.value.note?.let { shareNoteHelper.share(it) }
    }

    override fun onCleared() {
        playerController.release()
        super.onCleared()
    }
}
