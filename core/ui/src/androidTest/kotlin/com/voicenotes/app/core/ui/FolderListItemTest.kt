package com.voicenotes.app.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import org.junit.Rule
import org.junit.Test

class FolderListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun singleNoteUsesSingularLabel() {
        composeTestRule.setContent {
            VoiceNotesTheme { FolderListItem(name = "Shopping", noteCount = 1, onClick = {}) }
        }

        composeTestRule.onNodeWithText("1 note").assertIsDisplayed()
    }

    @Test
    fun multipleNotesUsePluralLabel() {
        composeTestRule.setContent {
            VoiceNotesTheme { FolderListItem(name = "Shopping", noteCount = 3, onClick = {}) }
        }

        composeTestRule.onNodeWithText("3 notes").assertIsDisplayed()
    }

    @Test
    fun zeroNotesUsePluralLabel() {
        composeTestRule.setContent {
            VoiceNotesTheme { FolderListItem(name = "Shopping", noteCount = 0, onClick = {}) }
        }

        composeTestRule.onNodeWithText("0 notes").assertIsDisplayed()
    }

    @Test
    fun clickingRowInvokesOnClick() {
        var clicked = false
        composeTestRule.setContent {
            VoiceNotesTheme { FolderListItem(name = "Shopping", noteCount = 2, onClick = { clicked = true }) }
        }

        composeTestRule.onNodeWithText("Shopping").performClick()

        assert(clicked)
    }
}
