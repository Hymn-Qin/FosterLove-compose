package com.example.foster.ui

import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.foster.ui.detail.DetailScreen
import com.example.foster.ui.home.HomeScreen
import com.example.foster.ui.theme.FosterTheme

// Start building your app here!
@Composable
fun FosterApp(viewModel: MainViewModel) {
    Crossfade(targetState = viewModel.currentScreen) { screen ->
        Surface(color = MaterialTheme.colors.background) {
            when(screen) {
                is Screen.Home -> HomeScreen(
                    repository = viewModel.repository,
                    navigateTo = viewModel::navigateTo
                )
                is Screen.Detail -> DetailScreen(
                    id = screen.palId,
                    repository = viewModel.repository,
                    onBack = { viewModel.onBack() },
                   )
            }
        }
    }

}

@Composable
fun FosterAppContent(viewModel: MainViewModel) {
    FosterTheme {
        FosterApp(viewModel = viewModel)
    }
}