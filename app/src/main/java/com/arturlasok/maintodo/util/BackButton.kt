package com.arturlasok.maintodo.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R


@Composable
fun BackButton(
    isDarkModeOn: Boolean,
    modifier: Modifier,
    onClick:() -> Unit
)
{

        Button(
            onClick = {
                onClick()
            },
            modifier = Modifier.padding(0.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = UiText.StringResource(
                    R.string.back,
                    "no"
                ).asString().uppercase(),
                style = MaterialTheme.typography.h4,

                )
        }




}
