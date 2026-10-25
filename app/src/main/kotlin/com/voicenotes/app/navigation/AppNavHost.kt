package com.voicenotes.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.voicenotes.app.feature.folders.FOLDERS_ROUTE
import com.voicenotes.app.feature.folders.FoldersRoute
import com.voicenotes.app.feature.notes.detail.noteDetailRoute
import com.voicenotes.app.feature.notes.detail.noteDetailScreen
import com.voicenotes.app.feature.notes.list.NOTES_LIST_ROUTE_PATTERN
import com.voicenotes.app.feature.notes.list.notesListRoute
import com.voicenotes.app.feature.notes.list.notesListScreen
import com.voicenotes.app.feature.record.recordRoute
import com.voicenotes.app.feature.record.recordScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    val selectedFolderId = if (currentRoute == NOTES_LIST_ROUTE_PATTERN) {
        currentEntry?.arguments?.getLong("folderId")
    } else null

    BoxWithConstraints(modifier.fillMaxSize()) {
        val isBrowsing = currentRoute == null || currentRoute == FOLDERS_ROUTE ||
            currentRoute == NOTES_LIST_ROUTE_PATTERN
        val showFolderPane = maxWidth >= 840.dp && isBrowsing

        val openFolder: (Long) -> Unit = { folderId ->
            if (selectedFolderId != folderId) {
                navController.navigate(notesListRoute(folderId)) {
                    // Switching folders replaces the notes destination rather than accumulating history.
                    popUpTo(FOLDERS_ROUTE)
                    launchSingleTop = true
                }
            }
        }

        BrowserPanes(
            showFolderPane = showFolderPane,
            folders = {
                FoldersRoute(
                    onOpenFolder = openFolder,
                    onRecord = { navController.navigate(recordRoute(null)) },
                    selectedFolderId = selectedFolderId,
                    showRecordAction = selectedFolderId == null,
                )
            },
        ) {
            // Keep one NavHost at one composition location as the window is resized.
            NavHost(
                navController = navController,
                startDestination = FOLDERS_ROUTE,
                modifier = Modifier.fillMaxSize(),
            ) {
                composable(FOLDERS_ROUTE) {
                    if (showFolderPane) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Select a folder to see its voice notes")
                        }
                    } else {
                        FoldersRoute(
                            onOpenFolder = openFolder,
                            onRecord = { navController.navigate(recordRoute(null)) },
                        )
                    }
                }
                notesListScreen(
                    showBack = !showFolderPane,
                    onBack = { navController.popBackStack() },
                    onRecord = { folderId -> navController.navigate(recordRoute(folderId)) },
                    onOpenNote = { noteId -> navController.navigate(noteDetailRoute(noteId)) },
                )
                noteDetailScreen(onBack = { navController.popBackStack() })
                recordScreen(onDone = { navController.popBackStack() })
            }
        }
    }
}

/** Stable content slot keeps navigation and recording state intact during window resizing. */
@Composable
internal fun BrowserPanes(
    showFolderPane: Boolean,
    folders: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Row(Modifier.fillMaxSize()) {
        if (showFolderPane) {
            Box(Modifier.width(360.dp).fillMaxHeight()) { folders() }
            VerticalDivider()
        }
        Box(Modifier.weight(1f).fillMaxHeight()) { content() }
    }
}
