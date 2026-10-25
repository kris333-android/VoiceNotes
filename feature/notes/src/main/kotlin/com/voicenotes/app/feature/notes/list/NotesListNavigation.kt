package com.voicenotes.app.feature.notes.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val NOTES_LIST_ROUTE_BASE = "notes"
const val NOTES_LIST_ROUTE_PATTERN = "$NOTES_LIST_ROUTE_BASE/{folderId}"

fun notesListRoute(folderId: Long): String = "$NOTES_LIST_ROUTE_BASE/$folderId"

fun NavGraphBuilder.notesListScreen(
    onBack: () -> Unit,
    onRecord: (folderId: Long?) -> Unit,
    onOpenNote: (noteId: Long) -> Unit,
    showBack: Boolean = true,
) {
    composable(
        route = NOTES_LIST_ROUTE_PATTERN,
        arguments = listOf(navArgument("folderId") { type = NavType.LongType }),
    ) {
        NotesListRoute(onBack = onBack, onRecord = onRecord, onOpenNote = onOpenNote, showBack = showBack)
    }
}
