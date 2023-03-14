package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R

@Composable
fun MoreButton(
    isDarkModeOn: Boolean,
    rotatedDown: Boolean,
    modifier: Modifier,
    onClick:() -> Unit,
    light_img: Int,
    dark_img: Int
    )
    {
        Image(
            bitmap = ImageBitmap.imageResource(
                id = if(isDarkModeOn) { dark_img } else {  light_img }
            ),
            modifier = modifier
                .size(
                    24.dp,
                    24.dp
                )
                .padding(0.dp)
                .rotate(if(rotatedDown) { 90.0f } else { 0.0f })
                .clickable(onClick = { onClick() })
            ,
            contentDescription = "Ustawienia",
            colorFilter = if(isDarkModeOn) { ColorFilter.tint(Color.White) } else {ColorFilter.tint(Color.White) }
            )



    }
