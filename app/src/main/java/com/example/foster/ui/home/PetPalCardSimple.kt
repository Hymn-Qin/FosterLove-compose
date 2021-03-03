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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foster.R
import com.example.foster.data.post.impl.pal3
import com.example.foster.model.PetPal
import com.example.foster.ui.Screen
import com.example.foster.ui.ThemedPreview

@Composable
fun SexAndAge(
    petPal: PetPal,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val textStyle = MaterialTheme.typography.body2
            Text(
                text = petPal.metadata.gender,
                style = textStyle
            )
            Text(
                text = " - ${petPal.metadata.age} age",
                style = textStyle
            )
        }
    }
}

@Preview("Bookmark Button")
@Composable
fun SexAndAgePreview() {
    ThemedPreview {
        Surface {
            SexAndAge(petPal = pal3)
        }
    }
}

@Composable
fun BookmarkButton(isBookmarked: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val clickLabel = stringResource(
        id = if (isBookmarked) R.string.unbookmark else R.string.bookmark
    )
    IconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onClick() },
        modifier = modifier.semantics {
            this.onClick(label = clickLabel, action = null)
        }
    ) {
        Icon(
            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = null
        )
    }
}

@Preview("Bookmark Button")
@Composable
fun BookmarkButtonPreview() {
    ThemedPreview {
        Surface {
            BookmarkButton(isBookmarked = false, onClick = { })
        }
    }
}

@Preview("Bookmark Button Bookmarked")
@Composable
fun BookmarkButtonBookmarkedPreview() {
    ThemedPreview {
        Surface {
            BookmarkButton(isBookmarked = true, onClick = { })
        }
    }
}

@Composable
fun PostTitle(petPal: PetPal) {
    Text(text = petPal.name, style = MaterialTheme.typography.subtitle1)
}

@Composable
fun PetPalImage(petPal: PetPal, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = petPal.imageId),
        contentDescription = null,
        modifier = modifier
            .size(90.dp, 60.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun PetPalCardSimple(
    petPal: PetPal,
    navigateTo: (Screen) -> Unit,
    isAdopt: Boolean,
    onToggleAdopt: () -> Unit
) {
    val bookmarkAction = stringResource(if (isAdopt) R.string.unbookmark else R.string.bookmark)

    Row(modifier = Modifier
        .clickable(onClick = { navigateTo(Screen.Detail(petPal.id)) })
        .padding(16.dp)
        .semantics {
            customActions = listOf(
                CustomAccessibilityAction(
                    label = bookmarkAction,
                    action = {
                        onToggleAdopt()
                        true
                    }
                )
            )
        }
    ) {
        PetPalImage(petPal = petPal, modifier = Modifier.padding(end = 16.dp))
        Column(modifier = Modifier.weight(1f)) {
            PostTitle(petPal)
            // address
            SexAndAge(petPal)

        }
        BookmarkButton(
            isBookmarked = isAdopt,
            onClick = onToggleAdopt,
            modifier = Modifier
        )
    }
}

@Preview("Simple pet pal card")
@Composable
fun SimplePetPalPreview() {
    ThemedPreview {
        PetPalCardSimple(
            petPal = pal3,
            navigateTo = { },
            isAdopt = false,
            onToggleAdopt = { })
    }
}

@Preview("Simple pet pal card dark theme")
@Composable
fun SimplePetPalDarkPreview() {
    ThemedPreview(darkTheme = true) {
        PetPalCardSimple(
            petPal = pal3,
            navigateTo = { },
            isAdopt = false,
            onToggleAdopt = { })
    }
}

@Composable
fun PetPalCardHistory(
    petPal: PetPal,
    navigateTo: (Screen) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = { navigateTo(Screen.Detail(petPal.id)) })
            .padding(16.dp)
    ) {
        PetPalImage(petPal = petPal, modifier = Modifier.padding(end = 16.dp))
        Column(modifier = Modifier.weight(1f)) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "BASED ON ADOPTION HISTORY",
                    style = MaterialTheme.typography.overline
                )
            }
            PostTitle(petPal)
            // other
            SexAndAge(
                petPal = petPal,
                modifier = Modifier.padding(top = 4.dp)
            )

        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.cd_more_actions)
            )
        }
    }
}

@Preview("History pet pal card")
@Composable
fun HistoryPetPalPreview() {
    ThemedPreview {
        PetPalCardHistory(
            petPal = pal3,
            navigateTo = { },
        )
    }
}

@Preview("History pet pal card dark theme")
@Composable
fun HistoryPetPalDarkPreview() {
    ThemedPreview(darkTheme = true) {
        PetPalCardHistory(
            petPal = pal3,
            navigateTo = { },
        )
    }
}