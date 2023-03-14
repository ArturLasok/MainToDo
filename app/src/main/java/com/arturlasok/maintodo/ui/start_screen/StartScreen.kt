package com.arturlasok.maintodo.ui.start_screen

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.util.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StartScreen(
    addAlarm:(time: Long,beganTime: Long, name: String,desc:String, token: String, id: Long) -> Unit,
    removeAlarm:(taskid: Int) -> Unit,
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Boolean,
    startViewModel:  StartViewModel  = hiltViewModel(),
    confirmationTaskSetting: Boolean,
    snackMessage: (snackMessage:String) -> Unit,
) {

    val response = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    val categoryRowState = rememberLazyListState()
    val itemColumnState = rememberLazyListState()
    val categoryList = startViewModel.categoriesFromRoom.collectAsState(initial = listOf()).value
    val taskItemsList = startViewModel.tasksFromRoom.collectAsState(initial = mutableListOf<ItemToDo>()).value
    val selectedCategory = startViewModel.selectedCategory.collectAsState(initial = "").value
    val startScreenUiState: StartScreenState = startViewModel.startScreenUiState.value
    val newTaskState by startViewModel.newTaskState.collectAsState()
    val newDateTimeState by startViewModel.newDateTimeState.collectAsState()
    val scope = rememberCoroutineScope()
    val removeTaskAlert = rememberSaveable { mutableStateOf(Pair(false,-1L)) }

    val scrollToItemZero = rememberSaveable { mutableStateOf(false) }
    val isAtTopOfItemListColumn by remember {
        derivedStateOf {
            itemColumnState.firstVisibleItemIndex == 0 && itemColumnState.firstVisibleItemScrollOffset == 0
        }
    }
/*
    LaunchedEffect(key1 = newDateTimeState, block = {
        Log.i(TAG,"alarm task date: ${millisToDate(TimeUnit.DAYS.toMillis(newDateTimeState.taskDate))}")
        Log.i(TAG,"alarm task hour: ${millisToHourOfDay(newDateTimeState.taskTime)}")
        Log.i(TAG,"alarm notifi date: ${millisToDate(TimeUnit.DAYS.toMillis(newDateTimeState.notDate))}")
        Log.i(TAG,"alarm notifi hour: ${millisToHourOfDay(newDateTimeState.notTime)}")

        Log.i(TAG,"->>>>>>> alarm sum task date & hour: ${millisToDateAndHour((TimeUnit.DAYS.toMillis(newDateTimeState.taskDate))+(newDateTimeState.taskTime-3600000))}")
        Log.i(TAG,"->>>>>>> alarm sum not date &  hour: ${millisToDateAndHour((TimeUnit.DAYS.toMillis(newDateTimeState.notDate))+(newDateTimeState.notTime-3600000))}")
    } )


 */
    //scroll to top of item list Log.i(TAG,"start recompose:")

    LaunchedEffect(key1 = scrollToItemZero.value, block ={
        if(scrollToItemZero.value) {
            try {
                itemColumnState.scrollToItem(0, 0)
                scrollToItemZero.value = false

            } catch (e: Exception) {
                //nothing
            }
        }
    } )


    //back handler if in new task form
    BackHandler(enabled = true) {
        if(startScreenUiState==StartScreenState.AddTask) {
            startViewModel.setStartScreenUiState(StartScreenState.Welcome)
        }
    }
    //Item list back to top button
    AnimatedVisibility(
        visible = !isAtTopOfItemListColumn && startScreenUiState == StartScreenState.Welcome,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.zIndex(1.0f)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1.0f)
                .padding(bottom = 100.dp, start = 0.dp),
            contentAlignment = Alignment.BottomCenter
        ) {

            FloatingActionButton(
                onClick = {

                scrollToItemZero.value = true

                },
                modifier = Modifier
                    .alpha(0.2f)
                    .zIndex(1.0f)
                    .padding(1.dp)

            ) {
                Column() {
                    Image(

                        painterResource(
                            R.drawable.left_arrow
                        ), "Floating action",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .rotate(90f),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                    )

                }

            }
        }

    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            //FAB
            if(startScreenUiState == StartScreenState.Welcome) {
                ExtendedFloatingActionButton(
                    backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.8f),
                    modifier = Modifier
                        .alpha(0.8f)
                        .padding(bottom = 10.dp),
                    icon = {
                        Image(
                            bitmap = ImageBitmap.imageResource(id = R.drawable.addpoint_dark),
                            contentDescription = UiText.StringResource(
                                R.string.floating_button_add,
                                "asd"
                            ).asString(),
                            modifier = Modifier
                                .size(24.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary)
                        )
                    },
                    text = {
                        Text(
                            text = UiText.StringResource(
                                R.string.floating_button_add,
                                "asd"
                            ).asString().uppercase(),
                            color = MaterialTheme.colors.onPrimary
                        )
                    },
                    onClick = {
                        if(categoryList.isEmpty()) {
                            snackMessage(UiText.StringResource(
                                R.string.add_categories_first,
                                "asd"
                            ).asString(startViewModel.getApplication().applicationContext)
                            )
                        } else {
                            startViewModel.setStartScreenUiState(StartScreenState.AddTask)
                        }
                    }
                )
            }
        },
        topBar = {},
        bottomBar = {},
        backgroundColor = Color.Transparent

    ) {
    Row() {
        //Category row horizontal
        if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            CategoryColumn(
                isDarkModeOn = isDarkModeOn,
                categoryRowState = categoryRowState,
                categoryList = categoryList,
                selectedCategory = selectedCategory,
                onClick = { itemToken -> startViewModel.setSelectedCategory(itemToken); scrollToItemZero.value = true },
                startScreenUiState = startScreenUiState,
                navigateTo = { route -> navigateTo(route) },
                numberOfItems = startViewModel.counter
            ) { categoryToken ->

                scope.launch {
                    startViewModel.getTaskCount(categoryToken).collect() { itemSum ->
                        val ind = startViewModel.counter.indexOfFirst {
                            it.first == categoryToken
                        }
                        startViewModel.counter[ind] = Pair(categoryToken, itemSum)
                    }
                }

            }


        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            Column(modifier = Modifier.height(48.dp)) {

                //Add task top bar
                AddTaskTopBar(
                    startScreenUiState = startScreenUiState,
                    isDarkModeOn = isDarkModeOn,
                    navigateTo = { route -> navigateTo(route) },
                    categoryId = selectedCategory,
                    close = { startViewModel.setStartScreenUiState(StartScreenState.Welcome) },
                    closeKeyboard = { keyboardController?.hide() }
                ) {
                    startViewModel.onNewTaskNameChange(newTaskState.taskName)
                    startViewModel.onNewTaskDescChange(newTaskState.taskDesc)
                    startViewModel.onNewTaskCategoryChange(newTaskState.taskCategory)
                    //vm verify
                    startViewModel.verifyForm().onEach { formDataState ->
                        // ok
                        formDataState.ok?.let {
                            snackMessage(
                                UiText.StringResource(
                                    R.string.addtask_form_save,
                                    "no"
                                ).asString(startViewModel.getApplication().applicationContext)
                            )
                            addAlarm(
                                (TimeUnit.DAYS.toMillis(newDateTimeState.notDate))+(newDateTimeState.notTime-3600000),
                                TimeUnit.DAYS.toMillis(newDateTimeState.taskDate)+(newDateTimeState.taskTime-3600000),
                                newTaskState.taskName,
                                newTaskState.taskDesc,
                                startViewModel.getLastItemSelected(),
                                startViewModel.getLastItemIdInRoom(),
                            )
                            startViewModel.resetNewTaskState()
                            startViewModel.setStartScreenUiState(StartScreenState.Welcome)
                            startViewModel.onNewTaskNameChange("")
                            startViewModel.onNewTaskDescChange("")
                            startViewModel.getTaskItemsFromRoom(selectedCategory)
                            startViewModel.selectedCategoryRowIndex.value = categoryList.indexOf(
                                categoryList.find {
                                    it.dCatToken == selectedCategory
                                }
                            )
                            categoryRowState.animateScrollToItem(startViewModel.selectedCategoryRowIndex.value)

                        }
                        // error
                        formDataState.error?.let {
                            //snack message with error
                            snackMessage(it)

                        }

                    }.launchIn(scope)

                }


                //Top info and settings button

                TopAndSettings(
                    startScreenUiState = startScreenUiState,
                    dateAndNameOfDay = startViewModel.dateWithNameOfDayWeek(),
                    navigateTo = { route -> navigateTo(route) },
                    selectedCategory = selectedCategory,
                    isDarkModeOn = isDarkModeOn
                )


            }
            //Category row vertical
            if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {


                CategoryRow(
                    isDarkModeOn = isDarkModeOn,
                    categoryRowState = categoryRowState,
                    categoryList = categoryList,
                    selectedCategory = selectedCategory,
                    onClick = { itemToken -> startViewModel.setSelectedCategory(itemToken); scrollToItemZero.value = true},
                    startScreenUiState = startScreenUiState,
                    navigateTo = { route -> navigateTo(route) },
                    numberOfItems = startViewModel.counter
                ) { categoryToken ->

                    scope.launch {
                        startViewModel.getTaskCount(categoryToken).collect() { itemSum ->
                            val ind = startViewModel.counter.indexOfFirst {
                                it.first == categoryToken
                            }
                            startViewModel.counter[ind] = Pair(categoryToken, itemSum)
                        }
                    }

                }


            }
            //Add task form

            AddTaskForm(
                startViewModel = startViewModel,
                onNewDateChange= startViewModel::setNewTaskDate,
                onNewTimeChange = startViewModel::setNewTaskTime,
                onNewNotDateChange=startViewModel::setNotTaskDate,
                onNewNotTimeChange = startViewModel::setNotTaskTime,
                onNewErrorDateTimeSet=startViewModel::setErrorTaskDateTime,
                startScreenUiState = startScreenUiState,
                isDarkModeOn = isDarkModeOn,
                categoryId = selectedCategory,
                newTaskName = newTaskState.taskName,
                newTaskDesc = newTaskState.taskDesc,
                onTaskNameChange = startViewModel::onNewTaskNameChange,
                onTaskDescChange = startViewModel::onNewTaskDescChange

            ) { keyboardController?.hide(); focusManager.clearFocus(true) }


            //Category details

            CategoryDetails(
                visible = startScreenUiState != StartScreenState.AddTask,
                selectedCategoryDetails = if (startViewModel.getOneFromCategoryList(selectedCategory).dCatId != null) {
                    startViewModel.getOneFromCategoryList(selectedCategory)
                } else {
                    CategoryToDo()
                },
                navigateTo = { route -> navigateTo(route) },
                isDarkModeOn = isDarkModeOn,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
            )

            //Category anim to selected category if back from other screen
            if (startViewModel.getOneFromCategoryList(selectedCategory).dCatId != null) {
                LaunchedEffect(key1 = true, block = {
                    startViewModel.selectedCategoryRowIndex.value = categoryList.indexOf(
                        categoryList.find {
                            it.dCatToken == selectedCategory
                        }
                    )
                    categoryRowState.animateScrollToItem(startViewModel.selectedCategoryRowIndex.value)
                })
            }




            //Task list delete alert
            if(removeTaskAlert.value.first) {
                RemoveAlert(
                    question =  UiText.StringResource(
                        R.string.delete_task_alert_question,
                        "no"
                    ).asString() ,
                    onYes = {
                        //remove
                        scope.launch {
                            val deleteResponse = startViewModel.deleteTask(removeTaskAlert.value.second)
                            deleteResponse.ok.let {
                                if(it==true) {
                                    removeTaskAlert.value = Pair(false,-1)
                                    snackMessage(UiText.StringResource(
                                        R.string.delete_task_snack_removed,
                                        "no"
                                    ).asString(startViewModel.getApplication().applicationContext))

                                }
                            }
                            deleteResponse.error.let {
                                if (it != null) {
                                    if(it.isNotEmpty()) {
                                        removeTaskAlert.value = Pair(false,-1)
                                        snackMessage(it)
                                    }
                                }
                            }
                        }

                    },
                    onCancel = {
                        //cancel
                        removeTaskAlert.value = Pair(false,-1) })
                {
                    //on dismiss
                    removeTaskAlert.value = Pair(false,-1)
                }
            }
            //task list
            Log.i(TAG,"start recompose:")

            TasksColumnLazy(

                startViewModel = startViewModel,
                isDarkModeOn = isDarkModeOn,
                itemColumnState = itemColumnState,
                tasksList = taskItemsList,
                onClickEdit = { itemId ->
                    startViewModel.setLastItemSelected(itemId)
                    navigateTo(Screen.EditTask.route)
                },
                onClickCheck = { item: ItemToDo, newVal: Boolean, _:Int, nearIndex: Int ->

                    Log.i(TAG,"Item: ${item.dItemTitle}")




                    if(newVal) {
                        //remove alarms
                        item.dItemId?.let { it1 -> removeAlarm(it1.toInt()); removeAlarm(it1.toInt().unaryMinus()); Log.i(TAG,"Item alarm remove: ${item.dItemTitle}") }
                    } else {
                        //add alarms
                        addAlarm(
                            item.dItemRemindTime,
                            item.dItemDeliveryTime,
                            item.dItemTitle,
                            item.dItemDescription,
                            item.dItemToken,
                            item.dItemId ?: 0)
                    }



                    val itemToChangeIndex = taskItemsList.indexOf(taskItemsList.find {
                        it.dItemToken == item.dItemToken
                    })


                    scope.launch {
                        //update item in db

                        response.value = startViewModel.updateTaskItemCompletion(
                                taskItemsList[itemToChangeIndex],
                                selectedCategory
                            )

                         if(response.value.isNotEmpty())
                         {


                             Log.i(TAG,"response real pos ${startViewModel.getin(response.value)}")

                            //scrolling
                            if(newVal) {
                                //itemColumnState.animateScrollToItem(nearIndex)

                            } else {

                                itemColumnState.animateScrollToItem(startViewModel.getin(response.value))
                            }
                        }



                    }

                },
                onClickDelete = {itemId ->

                    removeTaskAlert.value = Pair(true,itemId)

                },

                confirmationTaskSetting = confirmationTaskSetting,
                startScreenUiState = startScreenUiState,
                lastItemSelected = startViewModel.getLastItemSelected()
            )



        }

    }

    }

}