package com.voicenotes.app.feature.notes.detail

import androidx.lifecycle.SavedStateHandle
import com.voicenotes.app.core.data.audio.AudioPlayerController
import com.voicenotes.app.core.data.share.ShareNoteHelper
import com.voicenotes.app.feature.notes.fake.FakeFolderRepository
import com.voicenotes.app.feature.notes.fake.FakeNoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class NoteDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(
        noteRepository: FakeNoteRepository,
        folderRepository: FakeFolderRepository,
        noteId: Long,
        shareNoteHelper: ShareNoteHelper = mock(),
    ) = NoteDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf("noteId" to noteId)),
        noteRepository = noteRepository,
        folderRepository = folderRepository,
        playerController = AudioPlayerController(),
        shareNoteHelper = shareNoteHelper,
    )

    @Test
    fun `createFolderAndMoveNote creates the folder and moves the current note into it`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val noteId = noteRepository.addTestNote(folderId = null)
        val sut = viewModel(noteRepository, folderRepository, noteId)

        sut.createFolderAndMoveNote("Ideas")
        advanceUntilIdle()

        val createdFolder = folderRepository.currentFolders().single()
        assertEquals("Ideas", createdFolder.name)
        assertEquals(createdFolder.id, noteRepository.getNote(noteId)?.folderId)
    }

    @Test
    fun `move changes the note's folder`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val noteId = noteRepository.addTestNote(folderId = null)
        val folderId = folderRepository.createFolder("Work")
        val sut = viewModel(noteRepository, folderRepository, noteId)

        sut.move(folderId)
        advanceUntilIdle()

        assertEquals(folderId, noteRepository.getNote(noteId)?.folderId)
    }

    @Test
    fun `rename updates the note title`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val noteId = noteRepository.addTestNote(title = "Old title")
        val sut = viewModel(noteRepository, folderRepository, noteId)

        sut.rename("New title")
        advanceUntilIdle()

        assertEquals("New title", noteRepository.getNote(noteId)?.title)
    }

    @Test
    fun `delete removes the note`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val noteId = noteRepository.addTestNote()
        val sut = viewModel(noteRepository, folderRepository, noteId)

        sut.delete()
        advanceUntilIdle()

        assertNull(noteRepository.getNote(noteId))
    }
}
