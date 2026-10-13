package com.voicenotes.app.feature.notes.detail

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

class NoteDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val note = VoiceNote(
        id = 1L,
        title = "Meeting notes",
        filePath = "/tmp/note.m4a",
        folderId = null,
        durationMillis = 65_000L,
        sizeBytes = 1_000L,
        createdAt = Instant.now(),
    )

    private fun setContent(
        uiState: NoteDetailUiState = NoteDetailUiState(note = note),
        onBack: () -> Unit = {},
        onTogglePlayback: () -> Unit = {},
        onSeek: (Long) -> Unit = {},
        onRename: (String) -> Unit = {},
        onMove: (Long?) -> Unit = {},
        onCreateFolderAndMove: (String) -> Unit = {},
        onDelete: () -> Unit = {},
        onShare: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            VoiceNotesTheme {
                NoteDetailScreen(
                    uiState = uiState,
                    onBack = onBack,
                    onTogglePlayback = onTogglePlayback,
                    onSeek = onSeek,
                    onRename = onRename,
                    onMove = onMove,
                    onCreateFolderAndMove = onCreateFolderAndMove,
                    onDelete = onDelete,
                    onShare = onShare,
                )
            }
        }
    }

    @Test
    fun showsNoteTitle() {
        setContent()

        composeTestRule.onNodeWithText("Meeting notes").assertIsDisplayed()
    }

    @Test
    fun tappingPlayInvokesOnTogglePlayback() {
        var toggled = false
        setContent(onTogglePlayback = { toggled = true })

        composeTestRule.onNodeWithContentDescription("Play").performClick()

        assert(toggled)
    }

    @Test
    fun overflowRenameConfirmInvokesOnRename() {
        var renamedTitle: String? = null
        setContent(onRename = { renamedTitle = it })

        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("Rename").performClick()
        composeTestRule.onNodeWithText("Title").performTextReplacement("Standup notes")
        composeTestRule.onNodeWithText("Rename").performClick()

        assert(renamedTitle == "Standup notes")
    }

    @Test
    fun overflowDeleteConfirmInvokesOnDelete() {
        var deleted = false
        setContent(onDelete = { deleted = true })

        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.onNodeWithText("Delete note?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").performClick()

        assert(deleted)
    }

    @Test
    fun overflowShareInvokesOnShareDirectly() {
        var shared = false
        setContent(onShare = { shared = true })

        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("Share").performClick()

        assert(shared)
    }
}
