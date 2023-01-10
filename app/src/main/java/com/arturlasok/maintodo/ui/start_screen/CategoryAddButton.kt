package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.maintodo.R

@Composable
fun CategoryAddButton(
    modifier: Modifier,
    sizeImage: Int,
    sizeCircle: Int,
    image: Int,
    imageModifier: Modifier,
    isDarkModeOn: Boolean,
    clicked:() -> Unit


) {

    Column(
        modifier = modifier.width((sizeCircle).dp).clickable(onClick = {clicked()}),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier

                .size(sizeCircle.dp)
                .clip(CircleShape)
                .background(
                    if(isDarkModeOn) { MaterialTheme.colors.onSecondary } else { MaterialTheme.colors.onPrimary}
                ),

        ) {
            Image(
                contentScale = ContentScale.Crop,
                bitmap = ImageBitmap.imageResource(id = image),
                contentDescription = "Add new category",
                modifier = imageModifier
                    .size(sizeImage.dp)
                    .zIndex(0.9f),
                colorFilter =if(isDarkModeOn) ColorFilter.tint(MaterialTheme.colors.primaryVariant) else null
            )

        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Add new category",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center

        )
    }
}