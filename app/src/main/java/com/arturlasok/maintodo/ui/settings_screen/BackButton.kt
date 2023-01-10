package com.arturlasok.maintodo.ui.settings_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    Image(
        bitmap = ImageBitmap.imageResource(
            id = if(isDarkModeOn) { R.drawable.left_arrow } else { R.drawable.left_arrow }
        ),
        modifier = modifier
            .size(
                24.dp,
                24.dp
            )
            .padding(0.dp)
            .alpha(0.8f)
            .clickable(onClick = { onClick() })
        ,
        contentDescription = "Back",
        colorFilter = if(isDarkModeOn) { ColorFilter.tint(Color.White) } else { ColorFilter.tint(Color.Black) }
    )



}
