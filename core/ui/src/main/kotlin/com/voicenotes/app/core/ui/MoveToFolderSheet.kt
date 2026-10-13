package com.voicenotes.app.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.voicenotes.app.core.model.Folder

/**
 * Bottom sheet for moving a note into a different folder (or back to Unfiled).
 * [currentFolderId] is null when the note is currently unfiled.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveToFolderSheet(
    folders: List<Folder>,
    currentFolderId: Long?,
    onFolderSelected: (Long?) -> Unit,
    onCreateNewFolder: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Text(
            text = "Move to folder",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        LazyColumn(modifier = Modifier.navigationBarsPadding()) {
            item {
                FolderOptionRow(
                    icon = Icons.Filled.Inbox,
                    label = "Unfiled",
                    selected = currentFolderId == null,
                    onClick = { onFolderSelected(null) },
                )
            }
            items(folders, key = { it.id }) { folder ->
                FolderOptionRow(
                    icon = null,
                    label = folder.name,
                    selected = currentFolderId == folder.id,
                    onClick = { onFolderSelected(folder.id) },
                )
            }
            item {
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = false, onClick = onCreateNewFolder, role = Role.Button)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = "New folder",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun FolderOptionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
