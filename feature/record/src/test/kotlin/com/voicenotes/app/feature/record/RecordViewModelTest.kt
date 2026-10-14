package com.voicenotes.app.feature.record

import androidx.lifecycle.SavedStateHandle
import com.voicenotes.app.core.data.audio.AudioRecorderController
import com.voicenotes.app.core.data.audio.NoteFileStorage
import com.voicenotes.app.core.utils.DateFormattingUtils
import com.voicenotes.app.feature.record.fake.FakeNoteRepository
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class RecordViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun recorderController(
        stateFlow: MutableStateFlow<AudioRecorderController.State> = MutableStateFlow(AudioRecorderController.State.Idle),
        stopResult: AudioRecorderController.RecordingResult? = null,
    ): AudioRecorderController = mock {
        on { state } doReturn stateFlow
        on { stop() } doReturn stopResult
    }

    private fun fileStorage(file: File = File("/tmp/note_test.m4a")): NoteFileStorage = mock {
        on { createRecordingFile() } doReturn file
    }

    private fun viewModel(
        recorderController: AudioRecorderController,
        noteRepository: FakeNoteRepository = FakeNoteRepository(),
        fileStorage: NoteFileStorage = fileStorage(),
        folderId: Long = -1L,
        dateFormattingUtils: DateFormattingUtils = DateFormattingUtils(),
    ) = RecordViewModel(
        savedStateHandle = SavedStateHandle(mapOf("folderId" to folderId)),
        recorderController = recorderController,
        noteRepository = noteRepository,
        fileStorage = fileStorage,
        dateFormattingUtils = dateFormattingUtils,
    )

    @Test
    fun `startRecording creates a file and starts the controller`() = runTest(dispatcher) {
        val file = File("/tmp/note_start.m4a")
        val controller = recorderController()
        val sut = viewModel(controller, fileStorage = fileStorage(file))

        sut.startRecording()

        verify(controller).start(file)
        assertEquals(RecordUiState.Phase.RECORDING, sut.uiState.value.phase)
    }

    @Test
    fun `recording state updates elapsed time and amplitude history`() = runTest(dispatcher) {
        val stateFlow = MutableStateFlow<AudioRecorderController.State>(AudioRecorderController.State.Idle)
        val controller = recorderController(stateFlow)
        val sut = viewModel(controller)
        advanceUntilIdle()

        stateFlow.value = AudioRecorderController.State.Recording(elapsedMillis = 500L, amplitude = 16_383)
        advanceUntilIdle()

        val state = sut.uiState.value
        assertEquals(RecordUiState.Phase.RECORDING, state.phase)
        assertEquals(500L, state.elapsedMillis)
        assertEquals(1, state.amplitudeHistory.size)
        assertEquals(0.5f, state.amplitudeHistory.single(), 0.01f)
    }

    @Test
    fun `paused state freezes the reported elapsed time`() = runTest(dispatcher) {
        val stateFlow = MutableStateFlow<AudioRecorderController.State>(AudioRecorderController.State.Idle)
        val controller = recorderController(stateFlow)
        val sut = viewModel(controller)
        advanceUntilIdle()

        stateFlow.value = AudioRecorderController.State.Paused(elapsedMillis = 1_200L)
        advanceUntilIdle()

        val state = sut.uiState.value
        assertEquals(RecordUiState.Phase.PAUSED, state.phase)
        assertEquals(1_200L, state.elapsedMillis)
    }

    @Test
    fun `pauseRecording delegates to the controller`() = runTest(dispatcher) {
        val controller = recorderController()
        val sut = viewModel(controller)

        sut.pauseRecording()

        verify(controller).pause()
    }

    @Test
    fun `resumeRecording delegates to the controller`() = runTest(dispatcher) {
        val controller = recorderController()
        val sut = viewModel(controller)

        sut.resumeRecording()

        verify(controller).resume()
    }

    @Test
    fun `discardRecording cancels the controller and resets state`() = runTest(dispatcher) {
        val controller = recorderController()
        val sut = viewModel(controller)

        sut.discardRecording()

        verify(controller).cancel()
        assertEquals(RecordUiState(), sut.uiState.value)
    }

    @Test
    fun `finishAndSave saves a note with the recorded file and duration`() = runTest(dispatcher) {
        val recordedFile = File("/tmp/note_finished.m4a")
        val controller = recorderController(
            stopResult = AudioRecorderController.RecordingResult(recordedFile, durationMillis = 5_000L),
        )
        val noteRepository = FakeNoteRepository()
        val sut = viewModel(controller, noteRepository = noteRepository)

        sut.finishAndSave()
        advanceUntilIdle()

        val savedNoteId = sut.uiState.value.savedNoteId
        assertNotNull(savedNoteId)
        assertFalse(sut.uiState.value.isSaving)
        val savedNote = noteRepository.getNote(savedNoteId!!)
        assertEquals(recordedFile.absolutePath, savedNote?.filePath)
        assertEquals(5_000L, savedNote?.durationMillis)
    }

    @Test
    fun `finishAndSave assigns the note to the folder it was recorded in`() = runTest(dispatcher) {
        val controller = recorderController(
            stopResult = AudioRecorderController.RecordingResult(File("/tmp/note.m4a"), durationMillis = 1_000L),
        )
        val noteRepository = FakeNoteRepository()
        val sut = viewModel(controller, noteRepository = noteRepository, folderId = 7L)

        sut.finishAndSave()
        advanceUntilIdle()

        val savedNoteId = sut.uiState.value.savedNoteId
        assertEquals(7L, noteRepository.getNote(savedNoteId!!)?.folderId)
    }

    @Test
    fun `finishAndSave leaves the note unfiled when no folder id was passed`() = runTest(dispatcher) {
        val controller = recorderController(
            stopResult = AudioRecorderController.RecordingResult(File("/tmp/note.m4a"), durationMillis = 1_000L),
        )
        val noteRepository = FakeNoteRepository()
        val sut = viewModel(controller, noteRepository = noteRepository, folderId = -1L)

        sut.finishAndSave()
        advanceUntilIdle()

        val savedNoteId = sut.uiState.value.savedNoteId
        assertNull(noteRepository.getNote(savedNoteId!!)?.folderId)
    }

    @Test
    fun `finishAndSave does nothing when the controller has no active recording`() = runTest(dispatcher) {
        val controller = recorderController(stopResult = null)
        val noteRepository = FakeNoteRepository()
        val sut = viewModel(controller, noteRepository = noteRepository)

        sut.finishAndSave()
        advanceUntilIdle()

        assertTrue(noteRepository.allNotes().isEmpty())
        assertNull(sut.uiState.value.savedNoteId)
    }
}
