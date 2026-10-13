package com.voicenotes.app.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import org.junit.Rule
import org.junit.Test

class EmptyStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysTitleAndMessage() {
        composeTestRule.setContent {
            VoiceNotesTheme {
                EmptyState(
                    icon = Icons.Filled.GraphicEq,
                    title = "No voice notes yet",
                    message = "Tap Record to capture your first voice note here.",
                )
            }
        }

        composeTestRule.onNodeWithText("No voice notes yet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap Record to capture your first voice note here.").assertIsDisplayed()
    }
}
