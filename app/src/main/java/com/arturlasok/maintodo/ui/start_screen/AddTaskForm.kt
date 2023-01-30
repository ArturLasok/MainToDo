package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.admob.AdMobBigBanner
import kotlinx.coroutines.delay

@Composable
fun AddTaskForm(
    startScreenUiState: StartScreenState,
    isDarkModeOn: Boolean,
    categoryId: Long,
    newTaskName: String,
    newTaskDesc:String,
    onTaskNameChange:(text: String) -> Unit,
    onTaskDescChange:(text: String) -> Unit,
    hideKeyboard:() ->Unit,

) {
    val localVisible = rememberSaveable{ mutableStateOf(false) }
    LaunchedEffect(key1 = startScreenUiState, block = {

        if(localVisible.value) {
            localVisible.value = startScreenUiState == StartScreenState.AddTask
        } else {
            delay(1000)
            localVisible.value = startScreenUiState == StartScreenState.AddTask
        }
    })
    //Animate category details
    AnimatedVisibility(
        visible = localVisible.value,
        enter = slideInHorizontally(
            initialOffsetX = {
                it
            },
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing,
                delayMillis = 0
            )
        ),
        exit = slideOutHorizontally(
            targetOffsetX = {
                it - 3 * it
            },
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )

    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TaskNameForm(
                nameValue = newTaskName,
                onValueChange = { text ->  onTaskNameChange(text)},
                isDarkModeOn = isDarkModeOn,
                onDone = {hideKeyboard()}
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            TaskDescForm(
                nameValue = newTaskDesc,
                onValueChange = { text ->  onTaskDescChange(text)},
                isDarkModeOn = isDarkModeOn,
                onDone = {hideKeyboard()}
            )

            AdMobBigBanner()
        }
    }
}