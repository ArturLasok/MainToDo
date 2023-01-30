package com.arturlasok.maintodo.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TransparencyBox(height: Float, isDarkTheme:Boolean) {
    Column(modifier = Modifier.height(height.dp) ) {
        for(x:Int in 1..height.toInt()) {
            Row(Modifier.fillMaxWidth().background(
                if(isDarkTheme) {
                    Color(0xFF181414).copy(alpha = x * 0.05f)
                } else {
                    Color(0xFFFFEBFA).copy(alpha = x * 0.05f)
                }
            ).height(height.toInt().div(height.toInt()).dp)) {}

        }


    }
}