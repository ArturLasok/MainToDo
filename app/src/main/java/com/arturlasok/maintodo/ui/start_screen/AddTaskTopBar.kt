package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.util.BackButton
import com.arturlasok.maintodo.util.UiText
import kotlinx.coroutines.delay

@Composable
fun AddTaskTopBar(
    startScreenUiState: StartScreenState,
    isDarkModeOn: Boolean,
    navigateTo: (route: String) -> Unit,
    categoryId: String,
    close:() -> Unit,
    closeKeyboard:() -> Unit,
    verifyForm:() -> Unit
) {
    val localVisible = rememberSaveable{ mutableStateOf(false) }
    LaunchedEffect(key1 = startScreenUiState, block = {

        if(localVisible.value) {
            localVisible.value = startScreenUiState == StartScreenState.AddTask
        } else {
            delay(700)
            localVisible.value = startScreenUiState == StartScreenState.AddTask
        }
    })

    AnimatedVisibility(
        visible = localVisible.value,
        enter = slideInVertically(
            initialOffsetY = {
                it - 3*it
            },
            animationSpec = tween(
                durationMillis = 200,
                delayMillis = 0,
                easing = LinearOutSlowInEasing,
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                it - 3 * it
            },
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearOutSlowInEasing
            )
        )

    ) {
        Box() {

            //Back button
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth())
                {
                    BackButton(
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier,
                        onClick = { close() }
                    )
                   //add button
                   AddTaskButton(
                       hideKeyboard = { closeKeyboard() },
                       verifyForm = { verifyForm() }
                   )
                }
            }
            //Screen title
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp), contentAlignment = Alignment.Center) {

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