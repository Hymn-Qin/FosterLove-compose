/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            when (screen) {
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
