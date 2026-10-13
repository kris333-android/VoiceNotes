package com.voicenotes.app.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.voicenotes.app.core.common.formatDurationForAccessibility
import com.voicenotes.app.core.common.formatDurationMillis
import com.voicenotes.app.core.model.VoiceNote
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

@Composable
fun VoiceNoteListItem(
    note: VoiceNote,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateText = dateFormatter.format(note.createdAt.atZone(ZoneId.systemDefault()).toLocalDate())
    val durationText = formatDurationMillis(note.durationMillis)
    val accessibilityText = "Open ${note.title}, recorded $dateText, " +
        "${formatDurationForAccessibility(note.durationMillis)} long"

    ListItem(
        headlineContent = { Text(note.title) },
        supportingContent = { Text("$dateText • $durationText") },
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.GraphicEq,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        trailingContent = {
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options for ${note.title}",
                )
            }
        },
        modifier = modifier
            .clickable(onClickLabel = "Open voice note ${note.title}", onClick = onClick)
            .semantics { contentDescription = accessibilityText },
    )
}
