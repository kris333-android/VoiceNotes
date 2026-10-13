package com.voicenotes.app.feature.folders

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import com.voicenotes.app.core.model.ALL_NOTES_FOLDER_ID
import com.voicenotes.app.core.model.Folder
import com.voicenotes.app.core.model.FolderWithNoteCount
import com.voicenotes.app.core.model.ThemeMode
import com.voicenotes.app.core.model.UNFILED_FOLDER_ID
import java.time.Instant
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class FoldersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val shoppingFolder = Folder(id = 1L, name = "Shopping", createdAt = Instant.now())

    private fun setContent(
        uiState: FoldersUiState = FoldersUiState(),
        onOpenFolder: (Long) -> Unit = {},
        onRecord: () -> Unit = {},
        onCreateFolder: (String) -> Unit = {},
        onRenameFolder: (Long, String) -> Unit = { _, _ -> },
        onDeleteFolder: (Long) -> Unit = {},
        onThemeModeChange: (ThemeMode) -> Unit = {},
    ) {
        composeTestRule.setContent {
            VoiceNotesTheme {
                FoldersScreen(
                    uiState = uiState,
                    onOpenFolder = onOpenFolder,
                    onRecord = onRecord,
                    onCreateFolder = onCreateFolder,
                    onRenameFolder = onRenameFolder,
                    onDeleteFolder = onDeleteFolder,
                    onThemeModeChange = onThemeModeChange,
                )
            }
        }
    }

    @Test
    fun showsAllNotesRowWithCount() {
        setContent(uiState = FoldersUiState(allNotesCount = 5))

        composeTestRule.onNodeWithText("All notes").assertIsDisplayed()
        composeTestRule.onNodeWithText("5 notes").assertIsDisplayed()
    }

    @Test
    fun unfiledRowHiddenWhenCountIsZero() {
        setContent(uiState = FoldersUiState(unfiledCount = 0))

        composeTestRule.onAllNodesWithText("Unfiled").assertCountEquals(0)
    }

    @Test
    fun unfiledRowShownWhenCountIsPositive() {
        setContent(uiState = FoldersUiState(unfiledCount = 2))

        composeTestRule.onNodeWithText("Unfiled").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 notes").assertIsDisplayed()
    }

    @Test
    fun showsFolderRows() {
        setContent(uiState = FoldersUiState(folders = listOf(FolderWithNoteCount(shoppingFolder, 4))))

        composeTestRule.onNodeWithText("Shopping").assertIsDisplayed()
        composeTestRule.onNodeWithText("4 notes").assertIsDisplayed()
    }

    @Test
    fun tappingAllNotesInvokesOnOpenFolderWithSentinel() {
        var openedFolderId: Long? = null
        setContent(onOpenFolder = { openedFolderId = it })

        composeTestRule.onNodeWithText("All notes").performClick()

        assert(openedFolderId == ALL_NOTES_FOLDER_ID)
    }

    @Test
    fun tappingUnfiledInvokesOnOpenFolderWithSentinel() {
        var openedFolderId: Long? = null
        setContent(uiState = FoldersUiState(unfiledCount = 1), onOpenFolder = { openedFolderId = it })

        composeTestRule.onNodeWithText("Unfiled").performClick()

        assert(openedFolderId == UNFILED_FOLDER_ID)
    }

    @Test
    fun tappingRecordFabInvokesOnRecord() {
        var recordTapped = false
        setContent(onRecord = { recordTapped = true })

        composeTestRule.onNodeWithText("Record", useUnmergedTree = true).performClick()

        assert(recordTapped)
    }

    @Test
    fun creatingFolderViaToolbarInvokesOnCreateFolder() {
        var createdName: String? = null
        setContent(onCreateFolder = { createdName = it })

        composeTestRule.onNodeWithContentDescription("New folder").performClick()
        composeTestRule.onNodeWithText("Folder name").performTextInput("Travel")
        composeTestRule.onNodeWithText("Create").performClick()

        assert(createdName == "Travel")
    }

    @Test
    fun longPressFolderThenDeleteThenConfirmInvokesOnDeleteFolder() {
        var deletedFolderId: Long? = null
        setContent(
            uiState = FoldersUiState(folders = listOf(FolderWithNoteCount(shoppingFolder, 0))),
            onDeleteFolder = { deletedFolderId = it },
        )

        composeTestRule.onNodeWithText("Shopping").performTouchInput { longClick() }
        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.onNodeWithText("Delete folder?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").performClick()

        assert(deletedFolderId == shoppingFolder.id)
    }

    @Test
    fun selectingDarkThemeInvokesOnThemeModeChange() {
        var chosenMode: ThemeMode? = null
        setContent(onThemeModeChange = { chosenMode = it })

        composeTestRule.onNodeWithContentDescription("Change app theme").performClick()
        composeTestRule.onNodeWithText("Dark").performClick()

        assert(chosenMode == ThemeMode.DARK)
    }
}
