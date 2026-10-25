package com.voicenotes.app.feature.folders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.ui.semantics.selected
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.voicenotes.app.core.model.ALL_NOTES_FOLDER_ID
import com.voicenotes.app.core.model.Folder
import com.voicenotes.app.core.model.FolderWithNoteCount
import com.voicenotes.app.core.model.ThemeMode
import com.voicenotes.app.core.model.UNFILED_FOLDER_ID
import com.voicenotes.app.core.ui.ConfirmActionDialog
import com.voicenotes.app.core.ui.FolderListItem
import com.voicenotes.app.core.ui.TextInputDialog

@Composable
fun FoldersRoute(
    onOpenFolder: (folderId: Long) -> Unit,
    onRecord: () -> Unit,
    selectedFolderId: Long? = null,
    showRecordAction: Boolean = true,
    viewModel: FoldersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FoldersScreen(
        uiState = uiState,
        selectedFolderId = selectedFolderId,
        showRecordAction = showRecordAction,
        onOpenFolder = onOpenFolder,
        onRecord = onRecord,
        onCreateFolder = viewModel::createFolder,
        onRenameFolder = viewModel::renameFolder,
        onDeleteFolder = viewModel::deleteFolder,
        onThemeModeChange = viewModel::setThemeMode,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FoldersScreen(
    uiState: FoldersUiState,
    onOpenFolder: (folderId: Long) -> Unit,
    onRecord: () -> Unit,
    onCreateFolder: (String) -> Unit,
    onRenameFolder: (Long, String) -> Unit,
    onDeleteFolder: (Long) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
    selectedFolderId: Long? = null,
    showRecordAction: Boolean = true,
) {
    var showThemeMenu by remember { mutableStateOf(false) }
    var showCreateFolder by rememberSaveable { mutableStateOf(false) }
    var actionsFolder by remember { mutableStateOf<Folder?>(null) }
    var renameTarget by remember { mutableStateOf<Folder?>(null) }
    var deleteTarget by remember { mutableStateOf<Folder?>(null) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Voice Notes") },
                actions = {
                    IconButton(onClick = { showCreateFolder = true }) {
                        Icon(
                            Icons.Filled.CreateNewFolder,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "New folder"
                        )
                    }
                    IconButton(onClick = { showThemeMenu = true }) {
                        Icon(
                            Icons.Filled.Palette,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Change app theme"
                        )
                    }
                    DropdownMenu(
                        expanded = showThemeMenu,
                        onDismissRequest = { showThemeMenu = false }) {
                        ThemeMode.entries.forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode.label()) },
                                onClick = { onThemeModeChange(mode); showThemeMenu = false },
                                trailingIcon = {
                                    if (mode == uiState.themeMode) {
                                        Icon(Icons.Filled.Check, contentDescription = "Selected")
                                    }
                                },
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (showRecordAction) ExtendedFloatingActionButton(
                onClick = onRecord,
                icon = { Icon(Icons.Filled.Mic, contentDescription = null) },
                text = { Text("Record") },
            )
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                FolderListItem(
                    selected = selectedFolderId == ALL_NOTES_FOLDER_ID,
                    name = "All notes",
                    noteCount = uiState.allNotesCount,
                    icon = Icons.Filled.AllInbox,
                    onClick = { onOpenFolder(ALL_NOTES_FOLDER_ID) },
                )
            }
            if (uiState.unfiledCount > 0) {
                item {
                    FolderListItem(
                        selected = selectedFolderId == UNFILED_FOLDER_ID,
                        name = "Unfiled",
                        noteCount = uiState.unfiledCount,
                        icon = Icons.Filled.Inbox,
                        onClick = { onOpenFolder(UNFILED_FOLDER_ID) },
                    )
                }
            }
            items(uiState.folders, key = { it.folder.id }) { item ->
                FolderRow(
                    item = item,
                    isSelected = selectedFolderId == item.folder.id,
                    onClick = { onOpenFolder(item.folder.id) },
                    onLongClick = { actionsFolder = item.folder },
                )
            }
        }
    }

    actionsFolder?.let { folder ->
        FolderActionsSheet(
            onDismiss = { actionsFolder = null },
            onRename = { renameTarget = folder; actionsFolder = null },
            onDelete = { deleteTarget = folder; actionsFolder = null },
        )
    }

    if (showCreateFolder) {
        TextInputDialog(
            title = "New folder",
            label = "Folder name",
            confirmLabel = "Create",
            onDismiss = { showCreateFolder = false },
            onConfirm = { onCreateFolder(it); showCreateFolder = false },
        )
    }

    renameTarget?.let { folder ->
        TextInputDialog(
            title = "Rename folder",
            label = "Folder name",
            confirmLabel = "Rename",
            initialValue = folder.name,
            onDismiss = { renameTarget = null },
            onConfirm = { onRenameFolder(folder.id, it); renameTarget = null },
        )
    }

    deleteTarget?.let { folder ->
        ConfirmActionDialog(
            title = "Delete folder?",
            message = "\"${folder.name}\" will be deleted. Notes inside will move to Unfiled, not be deleted.",
            confirmLabel = "Delete",
            onDismiss = { deleteTarget = null },
            onConfirm = { onDeleteFolder(folder.id); deleteTarget = null },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FolderRow(
    item: FolderWithNoteCount,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val countLabel = if (item.noteCount == 1) "1 note" else "${item.noteCount} notes"
    val a11yLabel = "Open ${item.folder.name}, $countLabel. Long-press for more options."

    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surface,
        ),
        headlineContent = { Text(item.folder.name) },
        supportingContent = { Text(countLabel) },
        leadingContent = {
            Icon(
                Icons.Filled.Folder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            Icon(Icons.Filled.ChevronRight, contentDescription = null)
        },
        modifier = Modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                onClickLabel = a11yLabel
            )
            .semantics { contentDescription = a11yLabel; selected = isSelected },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderActionsSheet(onDismiss: () -> Unit, onRename: () -> Unit, onDelete: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        androidx.compose.foundation.layout.Column(modifier = Modifier.navigationBarsPadding()) {
            ListItem(
                headlineContent = { Text("Rename") },
                leadingContent = { Icon(Icons.Filled.Edit, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClickLabel = "Rename", onClick = onRename),
            )
            ListItem(
                headlineContent = { Text("Delete") },
                leadingContent = { Icon(Icons.Filled.Delete, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClickLabel = "Delete", onClick = onDelete),
            )
        }
    }
}

private fun ThemeMode.label(): String = when (this) {
    ThemeMode.SYSTEM -> "System default"
    ThemeMode.LIGHT -> "Light"
    ThemeMode.DARK -> "Dark"
}
