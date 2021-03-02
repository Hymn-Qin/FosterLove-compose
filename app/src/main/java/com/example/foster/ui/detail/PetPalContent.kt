package com.example.foster.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foster.data.post.impl.pal3
import com.example.foster.model.Metadata
import com.example.foster.model.PetPal
import com.example.foster.ui.ThemedPreview

private val defaultSpacerSize = 16.dp

@Composable
fun PetPalContent(petPal: PetPal, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = defaultSpacerSize)
    ) {
        item {
            Spacer(Modifier.height(defaultSpacerSize))
            PetPalHeaderImage(petPal = petPal)
        }
        item {
            Text(text = petPal.name, style = MaterialTheme.typography.h4)
            Spacer(Modifier.height(8.dp))
        }
        petPal.breeds.let { subtitle ->
            item {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.body2,
                        lineHeight = 20.sp
                    )
                }
                Spacer(Modifier.height(defaultSpacerSize))
            }
        }
        item {
            PetPalMetadata(petPal.metadata)
            Spacer(Modifier.height(24.dp))
        }
//        items(petPal.metadata.introduce) {
//            Paragraph(paragraph = it)
//        }
        item {
            Paragraph(paragraph = petPal.metadata.introduce)
        }
        item {
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun PetPalHeaderImage(petPal: PetPal) {
    val imageModifier = Modifier
        .heightIn(min = 180.dp)
        .fillMaxWidth()
        .clip(shape = MaterialTheme.shapes.medium)
    Image(
        painter = painterResource(petPal.imageId),
        contentDescription = null, // decorative
        modifier = imageModifier,
        contentScale = ContentScale.Crop
    )
    Spacer(Modifier.height(defaultSpacerSize))
}

@Composable
private fun PetPalMetadata(metadata: Metadata) {
    val typography = MaterialTheme.typography
    Row(
        // Merge semantics so accessibility services consider this row a single element
        modifier = Modifier.semantics(mergeDescendants = true) {}
    ) {

        Column {
            Spacer(Modifier.width(8.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "${metadata.gender} • ${metadata.age} age",
                    style = typography.caption
                )
            }
        }
    }
}

@Composable
private fun Paragraph(paragraph: String) {
    Box(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = paragraph,
            style = MaterialTheme.typography.h5
        )
    }
}

private val Colors.codeBlockBackground: Color
    get() = onSurface.copy(alpha = .15f)

@Preview("PetPal content")
@Composable
fun PreviewPetPal() {
    ThemedPreview {
        PetPalContent(petPal = pal3)
    }
}

@Preview("PetPal content dark theme")
@Composable
fun PreviewPetPalDark() {
    ThemedPreview(darkTheme = true) {
        PetPalContent(petPal = pal3)
    }
}