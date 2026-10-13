package com.voicenotes.app.feature.notes.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.voicenotes.app.core.common.formatDurationMillis
import com.voicenotes.app.core.data.audio.AudioPlayerController
import com.voicenotes.app.core.ui.ConfirmActionDialog
import com.voicenotes.app.core.ui.MoveToFolderSheet
import com.voicenotes.app.core.ui.TextInputDialog

@Composable
fun NoteDetailRoute(
    onBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) onBack()
    }

    NoteDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onTogglePlayback = viewModel::togglePlayback,
        onSeek = viewModel::seekTo,
        onRename = viewModel::rename,
        onMove = viewModel::move,
        onCreateFolderAndMove = viewModel::createFolderAndMoveNote,
        onDelete = viewModel::delete,
        onShare = viewModel::share,
    )
}

private fun AudioPlayerController.State.positionMillis(): Long = when (this) {
    is AudioPlayerController.State.Playing -> positionMillis
    is AudioPlayerController.State.Paused -> positionMillis
    is AudioPlayerController.State.Completed -> durationMillis
    AudioPlayerController.State.Idle -> 0L
}

private fun AudioPlayerController.State.durationMillis(): Long = when (this) {
    is AudioPlayerController.State.Playing -> durationMillis
    is AudioPlayerController.State.Paused -> durationMillis
    is AudioPlayerController.State.Completed -> durationMillis
    AudioPlayerController.State.Idle -> 0L
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    uiState: NoteDetailUiState,
    onBack: () -> Unit,
    onTogglePlayback: () -> Unit,
    onSeek: (Long) -> Unit,
    onRename: (String) -> Unit,
    onMove: (Long?) -> Unit,
    onCreateFolderAndMove: (String) -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val note = uiState.note ?: return

    var showMenu by remember { mutableStateOf(false) }
    var showRename by rememberSaveable { mutableStateOf(false) }
    var showMove by rememberSaveable { mutableStateOf(false) }
    var showCreateFolder by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

    val position = uiState.playerState.positionMillis()
    val duration = uiState.playerState.durationMillis().coerceAtLeast(1L)
    val isPlaying = uiState.playerState is AudioPlayerController.State.Playing

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(note.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Rename") },
                            leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                            onClick = { showMenu = false; showRename = true },
                        )
                        DropdownMenuItem(
                            text = { Text("Move to folder") },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.DriveFileMove, contentDescription = null) },
                            onClick = { showMenu = false; showMove = true },
                        )
                        DropdownMenuItem(
                            text = { Text("Share") },
                            leadingIcon = { Icon(Icons.Filled.Share, contentDescription = null) },
                            onClick = { showMenu = false; onShare() },
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = null) },
                            onClick = { showMenu = false; showDeleteConfirm = true },
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Slider(
                value = position.toFloat(),
                onValueChange = { onSeek(it.toLong()) },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Playback position" },
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(formatDurationMillis(position), style = MaterialTheme.typography.labelMedium)
                Text(formatDurationMillis(duration), style = MaterialTheme.typography.labelMedium)
            }

            FloatingActionButton(
                onClick = onTogglePlayback,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .size(80.dp)
                    .semantics { contentDescription = if (isPlaying) "Pause" else "Play" },
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                )
            }
        }
    }

    if (showRename) {
        TextInputDialog(
            title = "Rename note",
            label = "Title",
            confirmLabel = "Rename",
            initialValue = note.title,
            onDismiss = { showRename = false },
            onConfirm = { onRename(it); showRename = false },
        )
    }

    if (showMove && !showCreateFolder) {
        MoveToFolderSheet(
            folders = uiState.folders,
            currentFolderId = note.folderId,
            onFolderSelected = { onMove(it); showMove = false },
            onCreateNewFolder = { showCreateFolder = true },
            onDismiss = { showMove = false },
        )
    }

    if (showCreateFolder) {
        TextInputDialog(
            title = "New folder",
            label = "Folder name",
            confirmLabel = "Create",
            onDismiss = { showCreateFolder = false },
            onConfirm = { name ->
                onCreateFolderAndMove(name)
                showCreateFolder = false
                showMove = false
            },
        )
    }

    if (showDeleteConfirm) {
        ConfirmActionDialog(
            title = "Delete note?",
            message = "\"${note.title}\" will be permanently deleted.",
            confirmLabel = "Delete",
            onDismiss = { showDeleteConfirm = false },
            onConfirm = { onDelete(); showDeleteConfirm = false },
        )
    }
}
