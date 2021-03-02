package com.example.foster.ui.detail

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foster.R
import com.example.foster.data.post.PetPalsRepository
import com.example.foster.model.PetPal
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
        FunctionalityNotAvailablePopup { showDialog = false }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = petPal.name,
                        style = MaterialTheme.typography.subtitle2,
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
                onUnimplementedAction = { showDialog = true },
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
            IconButton(onClick = onUnimplementedAction) {
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
private fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = "Functionality not available \uD83D\uDE48",
                style = MaterialTheme.typography.body2
            )
        },
        confirmButton = {
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