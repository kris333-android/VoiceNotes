package com.voicenotes.app.feature.notes.list

import androidx.lifecycle.SavedStateHandle
import com.voicenotes.app.core.data.share.ShareNoteHelper
import com.voicenotes.app.core.model.ALL_NOTES_FOLDER_ID
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
class NotesListViewModelTest {

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
        folderId: Long = ALL_NOTES_FOLDER_ID,
    ) = NotesListViewModel(
        savedStateHandle = SavedStateHandle(mapOf("folderId" to folderId)),
        noteRepository = noteRepository,
        folderRepository = folderRepository,
        shareNoteHelper = mock<ShareNoteHelper>(),
    )

    @Test
    fun `createFolderAndMoveNote creates the folder and moves the note into it`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val noteId = noteRepository.addTestNote(title = "Groceries", folderId = null)
        val sut = viewModel(noteRepository, folderRepository)

        sut.createFolderAndMoveNote(noteId, "Shopping")
        advanceUntilIdle()

        val createdFolder = folderRepository.currentFolders().single()
        assertEquals("Shopping", createdFolder.name)
        assertEquals(createdFolder.id, noteRepository.getNote(noteId)?.folderId)
    }

    @Test
    fun `moveNote moves an existing note to the target folder`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val noteId = noteRepository.addTestNote(folderId = null)
        val folderId = folderRepository.createFolder("Work")
        val sut = viewModel(noteRepository, folderRepository)

        sut.moveNote(noteId, folderId)
        advanceUntilIdle()

        assertEquals(folderId, noteRepository.getNote(noteId)?.folderId)
    }

    @Test
    fun `moveNote back to null unfiles the note`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val folderId = folderRepository.createFolder("Work")
        val noteId = noteRepository.addTestNote(folderId = folderId)
        val sut = viewModel(noteRepository, folderRepository)

        sut.moveNote(noteId, null)
        advanceUntilIdle()

        assertNull(noteRepository.getNote(noteId)?.folderId)
    }

    @Test
    fun `renameNote updates the note title`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val noteId = noteRepository.addTestNote(title = "Old title")
        val sut = viewModel(noteRepository, folderRepository)

        sut.renameNote(noteId, "New title")
        advanceUntilIdle()

        assertEquals("New title", noteRepository.getNote(noteId)?.title)
    }

    @Test
    fun `deleteNote removes the note`() = runTest(dispatcher) {
        val noteRepository = FakeNoteRepository()
        val folderRepository = FakeFolderRepository()
        val noteId = noteRepository.addTestNote()
        val sut = viewModel(noteRepository, folderRepository)

        sut.deleteNote(noteId)
        advanceUntilIdle()

        assertNull(noteRepository.getNote(noteId))
    }
}
