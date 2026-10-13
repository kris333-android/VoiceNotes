package com.voicenotes.app.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/** A row representing either a real folder or a virtual collection (e.g. "All notes"). */
@Composable
fun FolderListItem(
    name: String,
    noteCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Folder,
) {
    val countLabel = if (noteCount == 1) "1 note" else "$noteCount notes"
    val a11yLabel = "Open $name, $countLabel"

    ListItem(
        headlineContent = { Text(name) },
        supportingContent = { Text(countLabel) },
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        trailingContent = {
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null)
        },
        modifier = modifier
            .clickable(onClickLabel = a11yLabel, onClick = onClick)
            .semantics { contentDescription = a11yLabel },
    )
}
