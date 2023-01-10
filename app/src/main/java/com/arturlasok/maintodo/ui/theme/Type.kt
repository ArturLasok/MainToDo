package com.arturlasok.maintodo.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),

    h1= TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
    ),
    h2 = TextStyle(

        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
    ),
    h3 = TextStyle(

        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    h4 = TextStyle(

        fontWeight = FontWeight.W600,
        fontSize = 12.sp,
    ),
    h5 = TextStyle(

        fontWeight = FontWeight.W600,
        fontSize = 10.sp,
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 8.sp,
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)