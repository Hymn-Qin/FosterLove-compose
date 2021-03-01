package com.example.foster.ui

import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.foster.ui.home.HomeScreen
import com.example.foster.ui.theme.FosterTheme

// Start building your app here!
@Composable
fun FosterApp(viewModel: MainViewModel) {
    Crossfade(targetState = viewModel.currentScreen) { screen ->
        Surface(color = MaterialTheme.colors.background) {
            when(screen) {
                Screen.Home -> HomeScreen(
                    navigateTo = viewModel::navigateTo
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