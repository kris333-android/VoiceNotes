package com.voicenotes.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import com.voicenotes.app.core.model.Folder
import com.voicenotes.app.core.model.FolderWithNoteCount
import com.voicenotes.app.core.model.VoiceNote
import com.voicenotes.app.feature.folders.FoldersScreen
import com.voicenotes.app.feature.folders.FoldersUiState
import com.voicenotes.app.feature.notes.list.NotesListScreen
import com.voicenotes.app.feature.notes.list.NotesListUiState
import java.time.Instant

@Preview(name = "Tablet landscape", widthDp = 1280, heightDp = 800, showBackground = true)
@Preview(name = "Tablet portrait", widthDp = 840, heightDp = 1100, showBackground = true)
@Composable
private fun TabletBrowserPreview() {
    BrowserPreview(twoPanes = true)
}

@Preview(name = "Phone notes", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun PhoneBrowserPreview() {
    BrowserPreview(twoPanes = false)
}

@Composable
private fun BrowserPreview(twoPanes: Boolean) {
    VoiceNotesTheme {
        BrowserPanes(
            showFolderPane = twoPanes,
            folders = {
                FoldersScreen(
                    uiState = FoldersUiState(
                        allNotesCount = 8, unfiledCount = 2,
                        folders = listOf(
                            FolderWithNoteCount(Folder(1, "Work", Instant.EPOCH), 4),
                            FolderWithNoteCount(Folder(2, "Ideas", Instant.EPOCH), 2),
                        ),
                        isLoading = false,
                    ),
                    selectedFolderId = 1, showRecordAction = false,
                    onOpenFolder = {}, onRecord = {}, onCreateFolder = {},
                    onRenameFolder = { _, _ -> }, onDeleteFolder = {}, onThemeModeChange = {},
                )
            },
        ) {
            NotesListScreen(
                uiState = NotesListUiState(
                    title = "Work",
                    notes = List(4) { index ->
                        VoiceNote(
                            id = index.toLong(), title = "Meeting ${index + 1}",
                            filePath = "", folderId = 1, durationMillis = 120_000,
                            sizeBytes = 1024, createdAt = Instant.parse("2026-01-01T12:00:00Z"),
                        )
                    },
                    isLoading = false,
                ),
                showBack = !twoPanes,
                onBack = {}, onRecord = {}, onOpenNote = {},
                onRename = { _, _ -> }, onMove = { _, _ -> }, onDelete = {},
                onShare = {}, onCreateFolderAndMove = { _, _ -> },
            )
        }
    }
}
