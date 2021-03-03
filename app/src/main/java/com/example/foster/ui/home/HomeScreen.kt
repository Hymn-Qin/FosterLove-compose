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

package com.example.foster.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foster.R
import com.example.foster.data.Result
import com.example.foster.data.post.PetPalsRepository
import com.example.foster.data.post.impl.BlockingFakePalsRepository
import com.example.foster.model.PetPal
import com.example.foster.ui.Screen
import com.example.foster.ui.SwipeToRefreshLayout
import com.example.foster.ui.ThemedPreview
import com.example.foster.ui.state.UiState
import com.example.foster.utils.produceUiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun HomeScreen(
    repository: PetPalsRepository,
    navigateTo: (Screen) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val (petPalsUiState, refreshPetPals, clearError) = produceUiState(repository) {
        getPetPals()
    }

    val adoptPetPals by repository.observeAdoption().collectAsState(setOf())
    val coroutineScope = rememberCoroutineScope()

    HomeScreen(
        petPal = petPalsUiState.value,
        onRefreshPetPals = refreshPetPals,
        onErrorDismiss = clearError,
        navigateTo = navigateTo,
        adoptPetPals = adoptPetPals,
        onToggleAdopt = {
            coroutineScope.launch { repository.toggleAdopt(it) }
        },
        scaffoldState = scaffoldState
    )

}

@Composable
fun HomeScreen(
    // 请求的宠物伙伴列表
    petPal: UiState<List<PetPal>>,
    // 刷新宠物伙伴列表
    onRefreshPetPals: () -> Unit,
    onErrorDismiss: () -> Unit,
    // 导航到其他屏幕
    navigateTo: (Screen) -> Unit,
    // 打开详情事件
    adoptPetPals: Set<String>,
    onToggleAdopt: (String) -> Unit,
    scaffoldState: ScaffoldState
) {
    // 显示一个错误
    if (petPal.hasError) {
        val errorMessage = stringResource(id = R.string.load_error)
        val retryMessage = stringResource(id = R.string.retry)

        val onRefreshPostsState by rememberUpdatedState(onRefreshPetPals)
        val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

        LaunchedEffect(scaffoldState) {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = retryMessage
            )
            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> onRefreshPostsState()
                SnackbarResult.Dismissed -> onErrorDismissState()
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.h6,
                        color = LocalContentColor.current
                    )
                }
            )
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            // 加载内容，根据posts 的状态显示不同内容
            LoadingContent(
                empty = petPal.initialLoad,
                emptyContent = {
                    FullScreenLoading()
                },
                loading = petPal.loading,
                onRefresh = onRefreshPetPals,
                content = {
                    // 主页
                    HomeScreenContent(
                        petPal = petPal,
                        onRefresh = onRefreshPetPals,
                        navigateTo = navigateTo,
                        adoptPetPals = adoptPetPals,
                        onToggleAdopt = onToggleAdopt,
                        modifier = modifier
                    )

                }
            )
        }
    )
}

@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        // 显示一个加载圈
        SwipeToRefreshLayout(
            refreshingState = loading,
            onRefresh = onRefresh,
            refreshIndicator = {
                Surface(elevation = 10.dp, shape = CircleShape) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(6.dp),
                        strokeWidth = 4.dp
                    )
                }
            },
            content = content,
        )
    }

}

@Composable
private fun HomeScreenContent(
    petPal: UiState<List<PetPal>>,
    onRefresh: () -> Unit,
    navigateTo: (Screen) -> Unit,
    adoptPetPals: Set<String>,
    onToggleAdopt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        petPal.data != null -> {
            PetPalList(petPals = petPal.data, navigateTo, adoptPetPals,  onToggleAdopt, modifier)
        }
        !petPal.hasError -> {
            // 请求数据为空 提示刷新
            TextButton(onClick = onRefresh, modifier = modifier.fillMaxSize()) {
                Text(text = "Tap to load content.", textAlign = TextAlign.Center)
            }
        }
        else -> {
            Box(modifier = modifier.fillMaxSize()) {
                /* empty screen */
            }
        }
    }
}

@Composable
fun PetPalList(
    petPals: List<PetPal>,
    navigateTo: (Screen) -> Unit,
    adoptPetPals: Set<String>,
    onToggleAdopt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val top = petPals[0]
    val simple = petPals.subList(0, 2)
    val popular = petPals.subList(2, 7)
    val history = petPals.subList(7, 10)

    LazyColumn(modifier = modifier) {
        item { PetPalListTopSection(petPal = top, navigateTo = navigateTo) }
        item {
            PetPalListSimpleSection(
                petPals = simple,
                navigateTo = navigateTo,
                adoptPetPals = adoptPetPals,
                onToggleAdopt = onToggleAdopt
            )
        }
        item { PetPalListPopularSection(petPals = popular, navigateTo = navigateTo) }
        item { PetPalListHistorySection(petPals = history, navigateTo = navigateTo) }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Home top
 */
@Composable
private fun PetPalListTopSection(petPal: PetPal, navigateTo: (Screen) -> Unit) {
    Text(
        text = "Top Pal For You",
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        style = MaterialTheme.typography.subtitle1
    )
    PetPalCardTop(
        petPal = petPal,
        modifier = Modifier.clickable(onClick = { navigateTo(Screen.Detail(palId = petPal.id)) })
    )
    PetPalListDivider()
}

@Composable
private fun PetPalListSimpleSection(
    petPals: List<PetPal>,
    navigateTo: (Screen) -> Unit,
    adoptPetPals: Set<String>,
    onToggleAdopt: (String) -> Unit
) {
    Column {
        petPals.forEach { petPal ->
            PetPalCardSimple(
                petPal = petPal,
                navigateTo = navigateTo,
                isAdopt = adoptPetPals.contains(petPal.id),
                onToggleAdopt = { onToggleAdopt(petPal.id) }
            )
            PetPalListDivider()
        }
    }
}

@Composable
private fun PetPalListPopularSection(petPals: List<PetPal>, navigateTo: (Screen) -> Unit) {
    Column {
        Text(
            text = "Popular On Pat Pal",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(modifier = Modifier.padding(end = 16.dp)) {
            items(petPals) { petPals ->
                PetPalCardPopular(petPal = petPals, navigateTo = navigateTo)
            }
        }
        PetPalListDivider()
    }
}

@Composable
private fun PetPalListHistorySection(petPals: List<PetPal>, navigateTo: (Screen) -> Unit) {
    Column {
        petPals.forEach { petPal ->
            PetPalCardHistory(petPal = petPal, navigateTo = navigateTo)
        }
        PetPalListDivider()
    }
}

@Composable
private fun PetPalListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}

/**
 * Home 组件预览
 */
@Preview("Home screen body preview.")
@Composable
fun PreviewHomeScreenBody() {
    ThemedPreview() {
        val post = loadFakePetPals()
        PetPalList(petPals = post, navigateTo = { }, adoptPetPals = setOf() , onToggleAdopt = { })
    }
}

@Composable
private fun loadFakePetPals(): List<PetPal> {
    val context = LocalContext.current
    val posts = runBlocking {
        BlockingFakePalsRepository(context).getPetPals()
    }
    return (posts as Result.Success).data
}