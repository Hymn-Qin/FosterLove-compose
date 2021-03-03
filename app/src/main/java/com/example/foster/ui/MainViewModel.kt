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

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.foster.data.post.PetPalsRepository
import com.example.foster.ui.ScreenName.DETAIL
import com.example.foster.ui.ScreenName.HOME
import com.example.foster.ui.ScreenName.valueOf
import com.example.foster.utils.getMutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class ScreenName { HOME, DETAIL }

sealed class Screen(val id: ScreenName) {
    object Home : Screen(HOME)
    data class Detail(val palId: String) : Screen(DETAIL)
}

private fun Screen.toBundle(): Bundle {
    return bundleOf(SIS_NAME to id.name).also {
        //
    }
}

private fun Bundle.toScreen(): Screen {
    val screenName = valueOf(getStringOrThrow(SIS_NAME))
    return when (screenName) {
        HOME -> Screen.Home
        else -> Screen.Detail("")
    }
}

private fun Bundle.getStringOrThrow(key: String) =
    requireNotNull(getString(key)) { "Missing key '$key' in $this ." }

private const val SIS_SCREEN = "sis_screen"
private const val SIS_NAME = "screen_name"
private const val SIS_POST = "post"

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: PetPalsRepository
) : ViewModel() {

    var currentScreen: Screen by savedStateHandle.getMutableStateOf<Screen>(
        key = SIS_SCREEN,
        default = Screen.Home,
        save = { it.toBundle() },
        restore = { it.toScreen() }
    )

    @MainThread
    fun onBack(): Boolean {
        val wasHandle = currentScreen != Screen.Home
        currentScreen = Screen.Home
        return wasHandle
    }

    @MainThread
    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }
}
