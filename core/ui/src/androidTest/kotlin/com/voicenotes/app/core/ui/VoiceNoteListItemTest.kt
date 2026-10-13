package com.voicenotes.app.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import com.voicenotes.app.core.model.VoiceNote
import java.time.Instant
import org.junit.Rule
import org.junit.Test

class VoiceNoteListItemTest {

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

    @Test
    fun displaysNoteTitle() {
        composeTestRule.setContent {
            VoiceNotesTheme { VoiceNoteListItem(note = note, onClick = {}, onMoreClick = {}) }
        }

        composeTestRule.onNodeWithText("Grocery list").assertIsDisplayed()
    }

    @Test
    fun clickingRowInvokesOnClick() {
        var clicked = false
        composeTestRule.setContent {
            VoiceNotesTheme { VoiceNoteListItem(note = note, onClick = { clicked = true }, onMoreClick = {}) }
        }

        composeTestRule.onNodeWithText("Grocery list").performClick()

        assert(clicked)
    }

    @Test
    fun clickingMoreOptionsInvokesOnMoreClickOnly() {
        var clicked = false
        var moreClicked = false
        composeTestRule.setContent {
            VoiceNotesTheme {
                VoiceNoteListItem(
                    note = note,
                    onClick = { clicked = true },
                    onMoreClick = { moreClicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("More options for Grocery list").performClick()

        assert(moreClicked)
        assert(!clicked)
    }
}
