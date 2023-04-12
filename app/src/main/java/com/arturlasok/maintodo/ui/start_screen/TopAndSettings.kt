package com.arturlasok.maintodo.ui.start_screen

import android.nfc.Tag
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.util.TAG
import com.arturlasok.maintodo.util.UiText
import kotlinx.coroutines.delay

@Composable
fun TopAndSettings(
    startScreenUiState: StartScreenState,
    dateAndNameOfDay:String,
    weekNumber: Int,
    navigateTo:(route:String)->Unit,
    selectedCategory: String,
    isDarkModeOn: Boolean
){
    val localVisible = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = startScreenUiState, block = {

        if(localVisible.value) {
            localVisible.value = startScreenUiState == StartScreenState.Welcome
        } else {
            delay(700)
            localVisible.value = startScreenUiState == StartScreenState.Welcome
        }
    })
    //title and settings
    AnimatedVisibility(
        visible = localVisible.value,
        enter = slideInVertically(
            initialOffsetY = {
                it - 3*it
            },
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearOutSlowInEasing,
            )
        ),

        exit = slideOutHorizontally(
            targetOffsetX = {
                it - 3 * it
            },
            animationSpec = tween(
                durationMillis = 700,
                easing = LinearOutSlowInEasing
            )
        )

    ) {
        Box() {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                //Hi Text
                Column {
/*
                    Text(
                        text = UiText.StringResource(
                            R.string.app_welcome,
                            "asd"
                        ).asString(),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primary
                    )

 */
                    Text(
                        text = dateAndNameOfDay,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary

                    )
                    Text(
                        text =  UiText.StringResource(R.string.week_number,"asd").asString() + " "+weekNumber,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary

                    )
                }
                //Settings Button
                Column {

                    SettingsButton(
                        tintDark = ColorFilter.tint(Color.White),
                        tintLight = ColorFilter.tint(Color.Black),
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier,
                        onClick = { navigateTo(Screen.Settings.route) },
                        light_img = R.drawable.settings,
                        dark_img = R.drawable.settings_dark
                    )


                }

            }
        }
    }
}