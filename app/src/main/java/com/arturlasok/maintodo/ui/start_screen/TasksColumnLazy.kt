package com.arturlasok.maintodo.ui.start_screen

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.util.*
import kotlinx.coroutines.delay



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksColumnLazy(
    startViewModel: StartViewModel,
    isDarkModeOn :Boolean,
    itemColumnState: LazyListState,
    tasksList: MutableList<ItemToDo>,
    onClickEdit:(itemToken: String) -> Unit,
    onClickCheck:(item:ItemToDo, newVal:Boolean, listIndex: Int, nearIndex: Int) -> Unit,
    onClickDelete:(itemId: Long) -> Unit,
    confirmationTaskSetting: Boolean,
    lastItemSelected: String,
    startScreenUiState: StartScreenState,
) {
    val localVisible = remember { mutableStateOf(false) }
    val openId = rememberSaveable { mutableStateOf(-1L) }

    LaunchedEffect(key1 = startScreenUiState, block = {

        if(localVisible.value) {
            localVisible.value = startScreenUiState == StartScreenState.Welcome
        } else {
            delay(700)
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
                durationMillis = 300,
                delayMillis = 0,
                easing = LinearOutSlowInEasing,
            )
        ),
        exit = slideOutHorizontally(
            targetOffsetX = {
                it - 3 * it
            },
            animationSpec = tween(
                durationMillis = 100,
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
        Box(modifier = Modifier
            .fillMaxSize()
            .zIndex(1.0f), contentAlignment = Alignment.BottomCenter) {
            TransparencyBox(height = 20f, isDarkTheme = isDarkModeOn)
        }

            LaunchedEffect(key1 = itemColumnState, block = {
                snapshotFlow {
                    itemColumnState.firstVisibleItemIndex
                }.collect {
                    startViewModel.setfirstViIndex(it) }
            } )
                DateInfoForBox(
                    tasksList = tasksList,
                    taskListSize =tasksList.size,
                    startViewModel = startViewModel,
                    sizeCircle = 38.dp,
                    isDarkModeOn = isDarkModeOn,
                    days = listOf(
                        UiText.StringResource(R.string.day7, "asd").asString(),
                        UiText.StringResource(R.string.day1, "asd").asString(),
                        UiText.StringResource(R.string.day2, "asd").asString(),
                        UiText.StringResource(R.string.day3, "asd").asString(),
                        UiText.StringResource(R.string.day4, "asd").asString(),
                        UiText.StringResource(R.string.day5, "asd").asString(),
                        UiText.StringResource(R.string.day6, "asd").asString(),

                        )
                )

      //  Log.i(TAG,"TCL alarm recompose -------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        LazyColumn(
            state = itemColumnState,
            modifier = Modifier
                .padding(4.dp)
                .zIndex(0.9f)
        ) {

            itemsIndexed(
                items = tasksList,
                key = { _, item ->  item.dItemToken }

            ) { index, item ->
       //         Log.i(TAG,"TASK alarm recompose ${item.dItemTitle}")
/*
                ListInfos(
                    months = startViewModel.months,
                    firstVisibleIndexX = derivedStateOf {  startViewModel.firstViItemIndex },
                    itemBefore = if(index>0) { tasksList[index-1] } else ItemToDo(),
                    itemNext = try {
                        tasksList[index + 1]
                    } catch (e:Exception) { ItemToDo()},
                    item = item,
                    modifier = Modifier,


                    )


 */

                SingleTaskItem(
                    firstVisibleIndexX = derivedStateOf {  startViewModel.firstViItemIndex },
                    itemColumnState = itemColumnState,
                    modifier = Modifier,
                    // .animateItemPlacement(animationSpec = tween(
                   //     durationMillis = 500,
                   //     easing = LinearOutSlowInEasing,
                   // )),
                    confirmationTaskSetting = confirmationTaskSetting,
                    itemBefore = if(index>0) { tasksList[index-1] } else ItemToDo(),
                    itemNext = try {
                        tasksList[index + 1]
                    } catch (e:Exception) { ItemToDo()},
                    item = item,
                    indexOfItem = index,
                    isDarkModeOn = isDarkModeOn,
                    checkChange = { newVal ->

                       onClickCheck(
                            item,
                            newVal,
                            itemColumnState.firstVisibleItemIndex,
                            if(index == tasksList.size) index-1 else {
                            if(index>0) index-1 else index }
                       )

                    },
                    deleteClick = { itemId ->  onClickDelete(itemId) },
                    editClick = { itemToken -> onClickEdit(itemToken) },
                    fullOpen = item.dItemId == openId.value && !item.dItemCompleted,
                    fviChange = { id -> startViewModel.setfirstViIndex(id); Log.i(TAG,"fvi change!!!!!! ${id}") },
                ) { id ->
                    if (openId.value == item.dItemId) {
                        openId.value = -1L
                    } else {
                        openId.value = id
                    }
                }


            }
            item { 
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp))
            }
        }

            //Item animate to last added or edited item
            if(lastItemSelected.isNotEmpty()) {
                LaunchedEffect(key1 = true, block = {
                    try {
                        val itemToScrollIndex = tasksList.sortedBy { it.dItemCompleted }.indexOfFirst {
                            it.dItemToken == lastItemSelected
                        }
                        Log.i(TAG,"Scroll to item: $lastItemSelected / indexOfElement: $itemToScrollIndex / tasklistSize: ${tasksList.size}")
                        itemColumnState.animateScrollToItem(itemToScrollIndex)
                    }
                    catch (e:Exception) {
                        Log.i(TAG,"Scroll error")
                    }
                } )
            }


    }
    }
}


