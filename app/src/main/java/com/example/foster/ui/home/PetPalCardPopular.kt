package com.example.foster.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foster.data.post.impl.pal3
import com.example.foster.model.PetPal
import com.example.foster.ui.Screen
import com.example.foster.ui.ThemedPreview

@Composable
fun PetPalCardPopular(
    petPal: PetPal,
    navigateTo: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(280.dp, 240.dp).padding(16.dp)
    ) {
        Column(modifier = Modifier.clickable(onClick = { navigateTo(Screen.Detail(petPal.id)) })) {
            Image(
                painter = painterResource(petPal.imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = petPal.name,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = petPal.breeds,
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${petPal.metadata.gender} - ${petPal.metadata.age} age",
                    style = MaterialTheme.typography.body2,
                )
            }
        }

    }
}

@Preview("Popular pet pal")
@Composable
fun PopularPetPalPreview() {
    ThemedPreview {
        Surface {
            PetPalCardPopular(petPal = pal3, navigateTo = { })
        }
    }
}

@Preview("Popular dark pet pal")
@Composable
fun PopularPetPalDarkPreview() {
    ThemedPreview(darkTheme = true) {
        Surface {
            PetPalCardPopular(petPal = pal3, navigateTo = { })
        }
    }
}