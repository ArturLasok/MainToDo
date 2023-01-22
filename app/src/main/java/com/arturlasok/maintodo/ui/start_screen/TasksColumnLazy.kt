package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.domain.model.ItemToDo
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksColumnLazy(
    isDarkModeOn :Boolean,
    itemColumnState: LazyListState,
    tasksList: SnapshotStateList<ItemToDo>,
    onClickEdit:(itemToken: String) -> Unit,
    onClickCheck:(itemToken: String, newVal:Boolean) -> Unit,
    startScreenUiState: StartScreenState
) {
    val localVisible = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = startScreenUiState, block = {

        if(localVisible.value) {
            localVisible.value = startScreenUiState == StartScreenState.Welcome
        } else {
            delay(1000)
            localVisible.value = startScreenUiState == StartScreenState.Welcome
        }
    })
    Spacer(modifier = Modifier.fillMaxWidth().height(6.dp))
    AnimatedVisibility(
        visible = localVisible.value,
        enter = slideInVertically(
            initialOffsetY = {
                it + 3*it
            },
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = 0,
                easing = LinearOutSlowInEasing,
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
        //Tasks Column
        LazyColumn(
            state = itemColumnState,
            modifier = Modifier.padding(4.dp)
        ) {

            itemsIndexed(
                items = tasksList.sortedBy { it.dItemCompleted },
                key = { _, item ->  item.dItemToken }

            ) { index, item ->

                SingleTaskItem(
                    modifier = Modifier.animateItemPlacement(animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                    )),
                    item = item,
                    indexOfItem = index,
                    isDarkModeOn = isDarkModeOn,
                    checkChange = { newVal -> onClickCheck(item.dItemToken,newVal)  }
                )
            }
        }
    }
}