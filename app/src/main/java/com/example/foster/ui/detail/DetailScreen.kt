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
package com.example.foster.ui.detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foster.R
import com.example.foster.data.post.PetPalsRepository
import com.example.foster.data.post.impl.pal3
import com.example.foster.model.PetPal
import com.example.foster.ui.ThemedPreview
import com.example.foster.ui.home.BookmarkButton
import com.example.foster.utils.produceUiState
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    id: String,
    repository: PetPalsRepository,
    onBack: () -> Unit
) {
    val (petPal) = produceUiState(repository) {
        getPetPal(id)
    }

    val petPalData = petPal.value.data ?: return

    val adoption by repository.observeAdoption().collectAsState(setOf())

    val isAdopt = adoption.contains(id)

    val coroutineScope = rememberCoroutineScope()

    DetailScreen(
        petPal = petPalData,
        onBack = onBack,
        isAdopt = isAdopt,
        onToggleAdopt = {
            coroutineScope.launch { repository.toggleAdopt(id) }
        }
    )
}

@Composable
private fun DetailScreen(
    petPal: PetPal,
    onBack: () -> Unit,
    isAdopt: Boolean,
    onToggleAdopt: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        FunctionalityNotAvailablePopup(
            "Successful adoption \uD83D\uDC4F",
            confirm = true,
            onConfirm = {
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = petPal.name,
                        style = MaterialTheme.typography.h6,
                        color = LocalContentColor.current
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_up)
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            PetPalContent(petPal, modifier)
        },
        bottomBar = {
            BottomBar(
                petPal = petPal,
                onUnimplementedAction = {
//                    popup.confirm = false
//                    popup.onConfirm = {  }
//                    popup.text = "Functionality not available \uD83D\uDE48"
                    showDialog = true
                },
                onAdoptAction = {
//                    popup.confirm = true
//                    popup.onConfirm = {
//
//                    }
//                    popup.text = " Successful adoption \uD83D\uDC4F"
                    showDialog = true
                },
                isAdopt = isAdopt,
                onToggleAdopt = onToggleAdopt
            )
        }
    )
}

@Composable
private fun BottomBar(
    petPal: PetPal,
    onUnimplementedAction: () -> Unit,
    onAdoptAction: () -> Unit,
    isAdopt: Boolean,
    onToggleAdopt: () -> Unit
) {
    Surface(elevation = 2.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = onAdoptAction) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.cd_add_to_favorites)
                )
            }
            BookmarkButton(
                isBookmarked = isAdopt,
                onClick = onToggleAdopt
            )
            val context = LocalContext.current
            IconButton(onClick = { sharePost(petPal, context) }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(R.string.cd_share)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onUnimplementedAction) {
                Icon(
                    painter = painterResource(R.drawable.ic_text_settings),
                    contentDescription = stringResource(R.string.cd_text_settings)
                )
            }
        }
    }
}

@Composable
private fun FunctionalityNotAvailablePopup(
    text: String,
    confirm: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.body2
            )
        },
        confirmButton = {
            if (confirm) {
                TextButton(onClick = onConfirm) {
                    Text(text = "OK")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "CLOSE")
            }
        }
    )
}

private fun sharePost(petPal: PetPal, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, petPal.name)
        putExtra(Intent.EXTRA_TEXT, petPal.breeds)
    }
    context.startActivity(Intent.createChooser(intent, "Share post"))
}

@Preview("Detail bottom bar")
@Composable
fun PreviewBottomBar() {
    ThemedPreview {
        BottomBar(
            petPal = pal3,
            onUnimplementedAction = { },
            onAdoptAction = { },
            isAdopt = false,
            onToggleAdopt = { }
        )
    }
}

@Preview("Popup dialog")
@Composable
fun PreviewPopup() {
    ThemedPreview {
        FunctionalityNotAvailablePopup("Functionality not available \uD83D\uDE48", true, { }, { })
    }
}
