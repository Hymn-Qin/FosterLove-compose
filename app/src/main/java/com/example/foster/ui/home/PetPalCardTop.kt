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
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foster.data.post.impl.pal1
import com.example.foster.model.PetPal
import com.example.foster.ui.ThemedPreview

@Composable
fun PetPalCardTop(petPal: PetPal, modifier: Modifier = Modifier) {
    val typography = MaterialTheme.typography
    // 最大宽度 padding
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        // 图片参数
        val imageModifier = Modifier
            .heightIn(min = 180.dp, max = 180.dp)
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
        Image(
            painter = painterResource(petPal.imageId),
            contentDescription = null, // decorative
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
        //间隙
        Spacer(Modifier.height(16.dp))

        Text(
            text = petPal.name,
            style = typography.h6
        )
        Text(
            text = petPal.breeds,
            style = typography.body2
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = "${petPal.metadata.gender} - ${petPal.metadata.age} age",
                style = typography.body2
            )
        }
    }
}

@Preview("Post card top")
@Composable
fun PreviewPostCardTop() {
    ThemedPreview {
        PetPalCardTop(petPal = pal1)
    }
}

@Preview("Post card top dark theme")
@Composable
fun PreviewPostCardTopDark() {
    ThemedPreview(darkTheme = true) {
        PetPalCardTop(petPal = pal1)
    }
}