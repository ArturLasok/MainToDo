package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.util.UiText

@Composable
fun TopAndSettings(
    startScreenUiState: StartScreenState,
    dateAndNameOfDay:String,
    navigateTo:(route:String)->Unit,
    selectedCategory: Long,
    isDarkModeOn: Boolean
){
    //title and settings
    AnimatedVisibility(
        visible = startScreenUiState == StartScreenState.Welcome,
        enter = slideInVertically(
            initialOffsetY = {
                it - 3*it
            },
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearOutSlowInEasing,
                delayMillis = 0
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                it - 3 * it
            },
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearOutSlowInEasing
            )
        )

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            //Hi Text
            Column {

                Text(
                    text = UiText.StringResource(
                        R.string.app_welcome,
                        "asd"
                    ).asString(),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = dateAndNameOfDay,
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary

                )
            }
            //Settings Button
            Column {

                SettingsButton(
                    isDarkModeOn = isDarkModeOn,
                    modifier = Modifier,
                    onClick = { navigateTo(Screen.Settings.route + "/${selectedCategory}") },
                    light_img = R.drawable.settings,
                    dark_img = R.drawable.settings_dark
                )


            }

        }
    }
}