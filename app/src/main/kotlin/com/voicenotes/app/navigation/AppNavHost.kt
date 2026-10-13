package com.voicenotes.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.voicenotes.app.feature.folders.FOLDERS_ROUTE
import com.voicenotes.app.feature.folders.foldersScreen
import com.voicenotes.app.feature.notes.detail.noteDetailRoute
import com.voicenotes.app.feature.notes.detail.noteDetailScreen
import com.voicenotes.app.feature.notes.list.notesListRoute
import com.voicenotes.app.feature.notes.list.notesListScreen
import com.voicenotes.app.feature.record.recordRoute
import com.voicenotes.app.feature.record.recordScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = FOLDERS_ROUTE,
        modifier = modifier,
    ) {
        foldersScreen(
            onOpenFolder = { folderId -> navController.navigate(notesListRoute(folderId)) },
            onRecord = { navController.navigate(recordRoute(folderId = null)) },
        )
        notesListScreen(
            onBack = { navController.popBackStack() },
            onRecord = { folderId -> navController.navigate(recordRoute(folderId)) },
            onOpenNote = { noteId -> navController.navigate(noteDetailRoute(noteId)) },
        )
        noteDetailScreen(onBack = { navController.popBackStack() })
        recordScreen(onDone = { navController.popBackStack() })
    }
}
