package com.voicenotes.app.feature.notes.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import com.voicenotes.app.core.model.VoiceNote
import java.time.Instant
import org.junit.Rule
import org.junit.Test

class NotesListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val note = VoiceNote(
        id = 1L,
        title = "Grocery list",
        filePath = "/tmp/note.m4a",
        folderId = null,
        durationMillis = 65_000L,
        sizeBytes = 1_000L,
        createdAt = Instant.now(),
    )

    private fun setContent(
        uiState: NotesListUiState = NotesListUiState(title = "All notes", isLoading = false),
        onBack: () -> Unit = {},
        onRecord: () -> Unit = {},
        onOpenNote: (Long) -> Unit = {},
        onRename: (Long, String) -> Unit = { _, _ -> },
        onMove: (Long, Long?) -> Unit = { _, _ -> },
        onDelete: (Long) -> Unit = {},
        onShare: (VoiceNote) -> Unit = {},
        onCreateFolderAndMove: (Long, String) -> Unit = { _, _ -> },
    ) {
        composeTestRule.setContent {
            VoiceNotesTheme {
                NotesListScreen(
                    uiState = uiState,
                    onBack = onBack,
                    onRecord = onRecord,
                    onOpenNote = onOpenNote,
                    onRename = onRename,
                    onMove = onMove,
                    onDelete = onDelete,
                    onShare = onShare,
                    onCreateFolderAndMove = onCreateFolderAndMove,
                )
            }
        }
    }

    @Test
    fun showsEmptyStateWhenNoNotes() {
        setContent(uiState = NotesListUiState(title = "All notes", notes = emptyList(), isLoading = false))

        composeTestRule.onNodeWithText("No voice notes yet").assertIsDisplayed()
    }

    @Test
    fun showsNoteWhenPresent() {
        setContent(uiState = NotesListUiState(title = "All notes", notes = listOf(note), isLoading = false))

        composeTestRule.onNodeWithText("Grocery list").assertIsDisplayed()
    }

    @Test
    fun tappingRecordFabInvokesOnRecord() {
        var recordTapped = false
        setContent(onRecord = { recordTapped = true })

        composeTestRule.onNodeWithText("Record", useUnmergedTree = true).performClick()

        assert(recordTapped)
    }

    @Test
    fun tappingNoteInvokesOnOpenNote() {
        var openedId: Long? = null
        setContent(
            uiState = NotesListUiState(title = "All notes", notes = listOf(note), isLoading = false),
            onOpenNote = { openedId = it },
        )

        composeTestRule.onNodeWithText("Grocery list").performClick()

        assert(openedId == note.id)
    }

    @Test
    fun overflowRenameConfirmInvokesOnRename() {
        var renamedId: Long? = null
        var renamedTitle: String? = null
        setContent(
            uiState = NotesListUiState(title = "All notes", notes = listOf(note), isLoading = false),
            onRename = { id, title -> renamedId = id; renamedTitle = title },
        )

        composeTestRule.onNodeWithContentDescription("More options for Grocery list").performClick()
        composeTestRule.onNodeWithText("Rename").performClick()
        composeTestRule.onNodeWithText("Title").performTextReplacement("Weekly list")
        composeTestRule.onNodeWithText("Rename").performClick()

        assert(renamedId == note.id)
        assert(renamedTitle == "Weekly list")
    }

    @Test
    fun overflowDeleteConfirmInvokesOnDelete() {
        var deletedId: Long? = null
        setContent(
            uiState = NotesListUiState(title = "All notes", notes = listOf(note), isLoading = false),
            onDelete = { deletedId = it },
        )

        composeTestRule.onNodeWithContentDescription("More options for Grocery list").performClick()
        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.onNodeWithText("Delete note?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").performClick()

        assert(deletedId == note.id)
    }

    @Test
    fun overflowShareInvokesOnShareDirectly() {
        var sharedNote: VoiceNote? = null
        setContent(
            uiState = NotesListUiState(title = "All notes", notes = listOf(note), isLoading = false),
            onShare = { sharedNote = it },
        )

        composeTestRule.onNodeWithContentDescription("More options for Grocery list").performClick()
        composeTestRule.onNodeWithText("Share").performClick()

        assert(sharedNote?.id == note.id)
    }
}
