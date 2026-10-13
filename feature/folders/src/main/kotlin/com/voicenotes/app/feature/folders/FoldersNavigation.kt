package com.voicenotes.app.feature.folders

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val FOLDERS_ROUTE = "folders"

fun NavGraphBuilder.foldersScreen(
    onOpenFolder: (folderId: Long) -> Unit,
    onRecord: () -> Unit,
) {
    composable(route = FOLDERS_ROUTE) {
        FoldersRoute(onOpenFolder = onOpenFolder, onRecord = onRecord)
    }
}
