package com.voicenotes.app.feature.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voicenotes.app.core.data.audio.AudioRecorderController
import com.voicenotes.app.core.data.audio.NoteFileStorage
import com.voicenotes.app.core.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import javax.inject.Inject

data class RecordUiState(
    val phase: Phase = Phase.IDLE,
    val elapsedMillis: Long = 0L,
    val amplitudeHistory: List<Float> = emptyList(),
    val isSaving: Boolean = false,
    val savedNoteId: Long? = null,
) {
    enum class Phase { IDLE, RECORDING, PAUSED }
}

private const val MAX_AMPLITUDE_HISTORY = 80
private const val MAX_RECORDER_AMPLITUDE = 32_767f

@HiltViewModel
class RecordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recorderController: AudioRecorderController,
    private val noteRepository: NoteRepository,
    private val fileStorage: NoteFileStorage,
) : ViewModel() {

    private val folderId: Long? = savedStateHandle.get<Long>("folderId")?.takeIf { it >= 0L }

    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    private var pendingFile: File? = null

    init {
        viewModelScope.launch {
            recorderController.state.collect { state ->
                when (state) {
                    is AudioRecorderController.State.Idle -> Unit
                    is AudioRecorderController.State.Recording -> _uiState.update {
                        it.copy(
                            phase = RecordUiState.Phase.RECORDING,
                            elapsedMillis = state.elapsedMillis,
                            amplitudeHistory = (it.amplitudeHistory + (state.amplitude / MAX_RECORDER_AMPLITUDE).coerceIn(0f, 1f))
                                .takeLast(MAX_AMPLITUDE_HISTORY),
                        )
                    }
                    is AudioRecorderController.State.Paused -> _uiState.update {
                        it.copy(phase = RecordUiState.Phase.PAUSED, elapsedMillis = state.elapsedMillis)
                    }
                }
            }
        }
    }

    fun startRecording() {
        val file = fileStorage.createRecordingFile()
        pendingFile = file
        recorderController.start(file)
        _uiState.value = RecordUiState(phase = RecordUiState.Phase.RECORDING)
    }

    fun pauseRecording() = recorderController.pause()

    fun resumeRecording() = recorderController.resume()

    fun discardRecording() {
        recorderController.cancel()
        pendingFile = null
        _uiState.value = RecordUiState()
    }

    fun finishAndSave() {
        val result = recorderController.stop() ?: return
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val title = "Voice note " + DateTimeFormatter.ofPattern("MMM d, h:mm a").format(LocalDateTime.now())
            val noteId = noteRepository.addNote(
                title = title,
                filePath = result.file.absolutePath,
                folderId = folderId,
                durationMillis = result.durationMillis,
                sizeBytes = result.file.length(),
            )
            _uiState.update { it.copy(isSaving = false, savedNoteId = noteId) }
        }
    }

    override fun onCleared() {
        recorderController.release()
        super.onCleared()
    }
}
