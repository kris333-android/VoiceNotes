package com.voicenotes.app.feature.folders

import com.voicenotes.app.core.data.preferences.UserPreferencesRepository
import com.voicenotes.app.core.model.ThemeMode
import com.voicenotes.app.feature.folders.fake.FakeFolderRepository
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class FoldersViewModelTest {

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
        folderRepository: FakeFolderRepository,
        userPreferencesRepository: UserPreferencesRepository = mock {
            on { themeMode } doReturn MutableStateFlow(ThemeMode.SYSTEM)
        },
    ) = FoldersViewModel(folderRepository, userPreferencesRepository)

    @Test
    fun `createFolder adds a folder with the given name`() = runTest(dispatcher) {
        val folderRepository = FakeFolderRepository()
        val sut = viewModel(folderRepository)

        sut.createFolder("Travel")
        advanceUntilIdle()

        val created = folderRepository.currentFolders().single()
        assertEquals("Travel", created.name)
    }

    @Test
    fun `renameFolder updates the folder name`() = runTest(dispatcher) {
        val folderRepository = FakeFolderRepository()
        val folderId = folderRepository.seedFolder("Old name")
        val sut = viewModel(folderRepository)

        sut.renameFolder(folderId, "New name")
        advanceUntilIdle()

        assertEquals("New name", folderRepository.currentFolders().single { it.id == folderId }.name)
    }

    @Test
    fun `deleteFolder removes the folder`() = runTest(dispatcher) {
        val folderRepository = FakeFolderRepository()
        val folderId = folderRepository.seedFolder("Temporary")
        val sut = viewModel(folderRepository)

        sut.deleteFolder(folderId)
        advanceUntilIdle()

        assertNull(folderRepository.currentFolders().find { it.id == folderId })
    }

    @Test
    fun `setThemeMode delegates to UserPreferencesRepository`() = runTest(dispatcher) {
        val folderRepository = FakeFolderRepository()
        val userPreferencesRepository = mock<UserPreferencesRepository> {
            on { themeMode } doReturn MutableStateFlow(ThemeMode.SYSTEM)
        }
        val sut = viewModel(folderRepository, userPreferencesRepository)

        sut.setThemeMode(ThemeMode.DARK)
        advanceUntilIdle()

        verify(userPreferencesRepository).setThemeMode(ThemeMode.DARK)
    }

    @Test
    fun `createFolder does not clobber existing folders`() = runTest(dispatcher) {
        val folderRepository = FakeFolderRepository()
        folderRepository.seedFolder("Existing")
        val sut = viewModel(folderRepository)

        sut.createFolder("New")
        advanceUntilIdle()

        assertTrue(folderRepository.currentFolders().map { it.name }.containsAll(listOf("Existing", "New")))
        assertEquals(2, folderRepository.currentFolders().size)
    }
}
