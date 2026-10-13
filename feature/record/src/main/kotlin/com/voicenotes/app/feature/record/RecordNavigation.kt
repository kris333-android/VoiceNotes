package com.voicenotes.app.feature.record

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

private const val RECORD_ROUTE_BASE = "record"
const val RECORD_ROUTE_PATTERN = "$RECORD_ROUTE_BASE?folderId={folderId}"

/** Route for recording a new note. Pass `folderId = null` to record into "Unfiled". */
fun recordRoute(folderId: Long?): String = "$RECORD_ROUTE_BASE?folderId=${folderId ?: -1L}"

fun NavGraphBuilder.recordScreen(onDone: () -> Unit) {
    composable(
        route = RECORD_ROUTE_PATTERN,
        arguments = listOf(navArgument("folderId") { type = NavType.LongType; defaultValue = -1L }),
    ) {
        RecordRoute(onDone = onDone)
    }
}
