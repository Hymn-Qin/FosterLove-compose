package com.example.foster.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foster.data.Result
import com.example.foster.data.post.impl.BlockingFakePalsRepository
import com.example.foster.model.PetPal
import com.example.foster.ui.Screen
import com.example.foster.ui.SwipeToRefreshLayout
import com.example.foster.ui.ThemedPreview
import com.example.foster.ui.state.UiState
import kotlinx.coroutines.runBlocking

@Composable
fun HomeScreen(
    navigateTo: (Screen) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
}

@Composable
fun HomeScreen(
    // 请求的宠物伙伴列表
    petPal: UiState<List<PetPal>>,
    // 刷新宠物伙伴列表
    onRefreshPosts: () -> Unit,
    // 导航到其他屏幕
    navigateTo: (Screen) -> Unit,
    // 打开详情事件
    onToggleDetails: (String) -> Unit,
    scaffoldState: ScaffoldState
) {
    // 显示一个错误
    if (petPal.hasError) {

    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

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
                onRefresh = onRefreshPosts,
                content = {
                    // 主页
                    HomeScreenContent(
                        petPal = petPal,
                        onRefresh = onRefreshPosts,
                        navigateTo = navigateTo,
                        onToggleDetails = onToggleDetails,
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
                            .size(36.dp)
                            .padding(6.dp)
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
    onToggleDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        petPal.data != null -> {
            PetPalList(petPals = petPal.data, navigateTo, onToggleDetails, modifier)
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
    onToggleDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val top = petPals[(0..(petPals.size)).random()]
    val simple = petPals.subList(0, 2)
    val popular = petPals.subList(2, 7)
    val history = petPals.subList(7, 10)

    LazyColumn(modifier = modifier) {
        item { PetPalListTopSection(petPal = top, navigateTo = navigateTo) }
        item {}
        item {}
        item {}
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

@Composable
private fun PetPalListTopSection(petPal: PetPal, navigateTo: (Screen) -> Unit) {
    Text(
        text = "Top pal for you.",
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        style = MaterialTheme.typography.subtitle1
    )
    PetPalCardTop(
        petPal = petPal,
        modifier = Modifier.clickable(onClick = { navigateTo(Screen.Detail(palId = petPal.id)) })
    )
}

@Preview("Home screen body preview.")
@Composable
fun PreviewHomeScreenBody() {
    ThemedPreview() {
        val post = loadFakePetPals()
        PetPalList(petPals = post, navigateTo = { }, onToggleDetails = { })

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