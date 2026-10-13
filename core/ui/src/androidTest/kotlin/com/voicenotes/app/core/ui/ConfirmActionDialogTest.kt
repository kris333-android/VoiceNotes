package com.voicenotes.app.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import org.junit.Rule
import org.junit.Test

class ConfirmActionDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsTitleAndMessage() {
        composeTestRule.setContent {
            VoiceNotesTheme {
                ConfirmActionDialog(
                    title = "Delete note?",
                    message = "\"Grocery list\" will be permanently deleted.",
                    confirmLabel = "Delete",
                    onDismiss = {},
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Delete note?").assertIsDisplayed()
        composeTestRule.onNodeWithText("\"Grocery list\" will be permanently deleted.").assertIsDisplayed()
    }

    @Test
    fun confirmButtonInvokesOnConfirm() {
        var confirmed = false
        composeTestRule.setContent {
            VoiceNotesTheme {
                ConfirmActionDialog(
                    title = "Delete note?",
                    message = "This cannot be undone.",
                    confirmLabel = "Delete",
                    onDismiss = {},
                    onConfirm = { confirmed = true },
                )
            }
        }

        composeTestRule.onNodeWithText("Delete").performClick()

        assert(confirmed)
    }

    @Test
    fun cancelButtonInvokesOnDismiss() {
        var dismissed = false
        composeTestRule.setContent {
            VoiceNotesTheme {
                ConfirmActionDialog(
                    title = "Delete note?",
                    message = "This cannot be undone.",
                    confirmLabel = "Delete",
                    onDismiss = { dismissed = true },
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Cancel").performClick()

        assert(dismissed)
    }
}
