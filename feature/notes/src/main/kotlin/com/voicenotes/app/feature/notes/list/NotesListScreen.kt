package com.voicenotes.app.feature.notes.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.voicenotes.app.core.model.VoiceNote
import com.voicenotes.app.core.ui.ConfirmActionDialog
import com.voicenotes.app.core.ui.EmptyState
import com.voicenotes.app.core.ui.MoveToFolderSheet
import com.voicenotes.app.core.ui.TextInputDialog
import com.voicenotes.app.core.ui.VoiceNoteListItem

@Composable
fun NotesListRoute(
    onBack: () -> Unit,
    onRecord: (folderId: Long?) -> Unit,
    onOpenNote: (noteId: Long) -> Unit,
    viewModel: NotesListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotesListScreen(
        uiState = uiState,
        onBack = onBack,
        onRecord = { onRecord(uiState.noteRecordFolderId) },
        onOpenNote = onOpenNote,
        onRename = viewModel::renameNote,
        onMove = viewModel::moveNote,
        onDelete = viewModel::deleteNote,
        onShare = viewModel::shareNote,
        onCreateFolderAndMove = viewModel::createFolderAndMoveNote,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    uiState: NotesListUiState,
    onBack: () -> Unit,
    onRecord: () -> Unit,
    onOpenNote: (noteId: Long) -> Unit,
    onRename: (noteId: Long, newTitle: String) -> Unit,
    onMove: (noteId: Long, folderId: Long?) -> Unit,
    onDelete: (noteId: Long) -> Unit,
    onShare: (VoiceNote) -> Unit,
    onCreateFolderAndMove: (noteId: Long, folderName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuNote by remember { mutableStateOf<VoiceNote?>(null) }
    var renameTarget by remember { mutableStateOf<VoiceNote?>(null) }
    var moveTarget by remember { mutableStateOf<VoiceNote?>(null) }
    var deleteTarget by remember { mutableStateOf<VoiceNote?>(null) }
    var showCreateFolder by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(uiState.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onRecord,
                icon = { Icon(Icons.Filled.Mic, contentDescription = null) },
                text = { Text("Record") },
            )
        },
    ) { padding ->
        if (uiState.notes.isEmpty() && !uiState.isLoading) {
            EmptyState(
                icon = Icons.Filled.GraphicEq,
                title = "No voice notes yet",
                message = "Tap Record to capture your first voice note here.",
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(uiState.notes, key = { it.id }) { note ->
                    VoiceNoteListItem(
                        note = note,
                        onClick = { onOpenNote(note.id) },
                        onMoreClick = { menuNote = note },
                    )
                }
            }
        }
    }

    menuNote?.let { note ->
        NoteOverflowMenu(
            onDismiss = { menuNote = null },
            onRename = { renameTarget = note; menuNote = null },
            onMove = { moveTarget = note; menuNote = null },
            onShare = { onShare(note); menuNote = null },
            onDelete = { deleteTarget = note; menuNote = null },
        )
    }

    renameTarget?.let { note ->
        TextInputDialog(
            title = "Rename note",
            label = "Title",
            confirmLabel = "Rename",
            initialValue = note.title,
            onDismiss = { renameTarget = null },
            onConfirm = { newTitle ->
                onRename(note.id, newTitle)
                renameTarget = null
            },
        )
    }

    moveTarget?.let { note ->
        if (!showCreateFolder) {
            MoveToFolderSheet(
                folders = uiState.folders,
                currentFolderId = note.folderId,
                onFolderSelected = { folderId ->
                    onMove(note.id, folderId)
                    moveTarget = null
                },
                onCreateNewFolder = { showCreateFolder = true },
                onDismiss = { moveTarget = null },
            )
        }
    }

    deleteTarget?.let { note ->
        ConfirmActionDialog(
            title = "Delete note?",
            message = "\"${note.title}\" will be permanently deleted.",
            confirmLabel = "Delete",
            onDismiss = { deleteTarget = null },
            onConfirm = {
                onDelete(note.id)
                deleteTarget = null
            },
        )
    }

    if (showCreateFolder) {
        val noteToMove = moveTarget
        TextInputDialog(
            title = "New folder",
            label = "Folder name",
            confirmLabel = "Create",
            onDismiss = { showCreateFolder = false },
            onConfirm = { name ->
                noteToMove?.let { onCreateFolderAndMove(it.id, name) }
                showCreateFolder = false
                moveTarget = null
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteOverflowMenu(
    onDismiss: () -> Unit,
    onRename: () -> Unit,
    onMove: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            NoteActionRow("Rename", Icons.Filled.Edit, onRename)
            NoteActionRow("Move to folder", Icons.AutoMirrored.Filled.DriveFileMove, onMove)
            NoteActionRow("Share", Icons.Filled.Share, onShare)
            NoteActionRow("Delete", Icons.Filled.Delete, onDelete)
        }
    }
}

@Composable
private fun NoteActionRow(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(label) },
        leadingContent = { Icon(icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClickLabel = label, onClick = onClick),
    )
}
