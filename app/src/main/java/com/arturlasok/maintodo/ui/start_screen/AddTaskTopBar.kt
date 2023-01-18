package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.util.BackButton
import com.arturlasok.maintodo.util.UiText

@Composable
fun AddTaskTopBar(
    startScreenUiState: StartScreenState,
    isDarkModeOn: Boolean,
    navigateTo: (route: String) -> Unit,
    categoryId: Long,
    close:() -> Unit
) {
    AnimatedVisibility(
        visible = startScreenUiState == StartScreenState.AddTask,
        enter = slideInVertically(
            initialOffsetY = {
                it - 3*it
            },
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearOutSlowInEasing,
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                it - 3 * it
            },
            animationSpec = tween(
                durationMillis = 50,
                easing = LinearOutSlowInEasing
            )
        )

    ) {
        Box() {

            //Back button
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth())
                {
                    BackButton(
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier,
                        onClick = { close() }
                    )
                   //add button
                }
            }
            //Screen title
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {

                Text(
                    text = UiText.StringResource(
                        R.string.addnewtask,
                        "no"
                    ).asString(),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.primary
                )
            }

        }
    }
}