package com.voicenotes.app.feature.notes.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val NOTE_DETAIL_ROUTE_BASE = "note"
const val NOTE_DETAIL_ROUTE_PATTERN = "$NOTE_DETAIL_ROUTE_BASE/{noteId}"

fun noteDetailRoute(noteId: Long): String = "$NOTE_DETAIL_ROUTE_BASE/$noteId"

fun NavGraphBuilder.noteDetailScreen(onBack: () -> Unit) {
    composable(
        route = NOTE_DETAIL_ROUTE_PATTERN,
        arguments = listOf(navArgument("noteId") { type = NavType.LongType }),
    ) {
        NoteDetailRoute(onBack = onBack)
    }
}
