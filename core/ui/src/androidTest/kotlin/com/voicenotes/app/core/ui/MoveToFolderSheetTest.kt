package com.voicenotes.app.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import com.voicenotes.app.core.model.Folder
import java.time.Instant
import org.junit.Rule
import org.junit.Test

class MoveToFolderSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val folders = listOf(
        Folder(id = 1L, name = "Shopping", createdAt = Instant.now()),
        Folder(id = 2L, name = "Work", createdAt = Instant.now()),
    )

    @Test
    fun showsUnfiledAndAllFolderNames() {
        composeTestRule.setContent {
            VoiceNotesTheme {
                MoveToFolderSheet(
                    folders = folders,
                    currentFolderId = null,
                    onFolderSelected = {},
                    onCreateNewFolder = {},
                    onDismiss = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Unfiled").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shopping").assertIsDisplayed()
        composeTestRule.onNodeWithText("Work").assertIsDisplayed()
    }

    @Test
    fun selectingUnfiledInvokesOnFolderSelectedWithNull() {
        var selected: Long? = -1L
        composeTestRule.setContent {
            VoiceNotesTheme {
                MoveToFolderSheet(
                    folders = folders,
                    currentFolderId = 1L,
                    onFolderSelected = { selected = it },
                    onCreateNewFolder = {},
                    onDismiss = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Unfiled").performClick()

        assert(selected == null)
    }

    @Test
    fun selectingAFolderInvokesOnFolderSelectedWithItsId() {
        var selected: Long? = null
        composeTestRule.setContent {
            VoiceNotesTheme {
                MoveToFolderSheet(
                    folders = folders,
                    currentFolderId = null,
                    onFolderSelected = { selected = it },
                    onCreateNewFolder = {},
                    onDismiss = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Work").performClick()

        assert(selected == 2L)
    }

    @Test
    fun tappingNewFolderInvokesOnCreateNewFolder() {
        var createNewFolderTapped = false
        composeTestRule.setContent {
            VoiceNotesTheme {
                MoveToFolderSheet(
                    folders = folders,
                    currentFolderId = null,
                    onFolderSelected = {},
                    onCreateNewFolder = { createNewFolderTapped = true },
                    onDismiss = {},
                )
            }
        }

        composeTestRule.onNodeWithText("New folder").performClick()

        assert(createNewFolderTapped)
    }
}
