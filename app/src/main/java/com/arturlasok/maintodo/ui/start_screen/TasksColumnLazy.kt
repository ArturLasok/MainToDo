package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.util.TransparencyBox
import com.arturlasok.maintodo.util.UiText
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksColumnLazy(
    isDarkModeOn :Boolean,
    itemColumnState: LazyListState,
    tasksList: SnapshotStateList<ItemToDo>,
    onClickEdit:(itemId: Long) -> Unit,
    onClickCheck:(itemToken: String, newVal:Boolean, listIndex: Int, nearIndex: Int) -> Unit,
    onClickDelete:(itemId: Long) -> Unit,
    confirmationTaskSetting: Boolean,
    startScreenUiState: StartScreenState,
) {
    val localVisible = remember { mutableStateOf(false) }
    val openId = rememberSaveable { mutableStateOf(-1L) }

    LaunchedEffect(key1 = startScreenUiState, block = {

        if(localVisible.value) {
            localVisible.value = startScreenUiState == StartScreenState.Welcome
        } else {
            delay(1000)
            localVisible.value = startScreenUiState == StartScreenState.Welcome
        }
    })
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(6.dp))
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
        //Empty tasks
        if(tasksList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Text(text= UiText.StringResource(
                    R.string.no_tasks,
                    "no"
                ).asString(),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.primary)
            }
        }
        //Tasks Column
        Box(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().zIndex(1.0f), contentAlignment = Alignment.BottomCenter) {
            TransparencyBox(height = 20f, isDarkTheme = isDarkModeOn)
        }    
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
                    confirmationTaskSetting = confirmationTaskSetting,
                    item = item,
                    indexOfItem = index,
                    isDarkModeOn = isDarkModeOn,
                    checkChange = { newVal ->

                       onClickCheck(
                            item.dItemToken,
                            newVal,
                            itemColumnState.firstVisibleItemIndex,
                            if(index == tasksList.size) index-1 else {
                            if(index>0) index-1 else index }
                       )

                    },
                    deleteClick = { itemId ->  onClickDelete(itemId) },
                    editClick = { itemId -> onClickEdit(itemId) },
                    fullOpen = item.dItemId == openId.value,
                    setOpen = { id -> if(openId.value==item.dItemId) { openId.value = -1L} else { openId.value = id}}
                )
                
            }
            item { 
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp))
            }
        }
    }
    }
}