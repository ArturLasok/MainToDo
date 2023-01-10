package com.arturlasok.maintodo.ui.start_screen

import android.util.Log.i
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
fun SettingButton(
    isDarkModeOn: Boolean,
    modifier: Modifier,
    onClick:() -> Unit
    )
    {
        Image(
            bitmap = ImageBitmap.imageResource(
                id = if(isDarkModeOn) {R.drawable.settings_dark } else {  R.drawable.settings }
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
            contentDescription = "Ustawienia",
            colorFilter = if(isDarkModeOn) { ColorFilter.tint(Color.White) } else {ColorFilter.tint(Color.Black) }
            )



    }
