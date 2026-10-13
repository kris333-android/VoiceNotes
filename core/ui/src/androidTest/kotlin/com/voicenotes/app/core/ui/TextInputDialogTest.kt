package com.voicenotes.app.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import org.junit.Rule
import org.junit.Test

class TextInputDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsInitialValuePreFilled() {
        composeTestRule.setContent {
            VoiceNotesTheme {
                TextInputDialog(
                    title = "Rename note",
                    label = "Title",
                    confirmLabel = "Rename",
                    initialValue = "Old title",
                    onDismiss = {},
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Old title").assertIsDisplayed()
    }

    @Test
    fun confirmButtonDisabledWhenTextIsBlank() {
        composeTestRule.setContent {
            VoiceNotesTheme {
                TextInputDialog(
                    title = "New folder",
                    label = "Folder name",
                    confirmLabel = "Create",
                    onDismiss = {},
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Create").assertIsNotEnabled()
    }

    @Test
    fun typingEnablesConfirmAndInvokesOnConfirmWithTrimmedText() {
        var confirmed: String? = null
        composeTestRule.setContent {
            VoiceNotesTheme {
                TextInputDialog(
                    title = "New folder",
                    label = "Folder name",
                    confirmLabel = "Create",
                    onDismiss = {},
                    onConfirm = { confirmed = it },
                )
            }
        }

        composeTestRule.onNodeWithText("Folder name").performTextInput("Travel  ")
        composeTestRule.onNodeWithText("Create").assertIsEnabled().performClick()

        assert(confirmed == "Travel")
    }

    @Test
    fun clearingTextDisablesConfirmAgain() {
        composeTestRule.setContent {
            VoiceNotesTheme {
                TextInputDialog(
                    title = "Rename folder",
                    label = "Folder name",
                    confirmLabel = "Rename",
                    initialValue = "Shopping",
                    onDismiss = {},
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Shopping").performTextClearance()

        composeTestRule.onNodeWithText("Rename").assertIsNotEnabled()
    }

    @Test
    fun cancelInvokesOnDismiss() {
        var dismissed = false
        composeTestRule.setContent {
            VoiceNotesTheme {
                TextInputDialog(
                    title = "New folder",
                    label = "Folder name",
                    confirmLabel = "Create",
                    onDismiss = { dismissed = true },
                    onConfirm = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Cancel").performClick()

        assert(dismissed)
    }
}
