package com.arturlasok.maintodo.ui.start_screen

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.util.UiText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StartScreen(
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Boolean,
    startViewModel:  StartViewModel  = hiltViewModel(),
    selectedFromNav: Long,
    snackMessage: (snackMessage:String) -> Unit,
) {


    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    val categoryRowState = rememberLazyListState()
    val itemColumnState = rememberLazyListState()
    val categoryList = startViewModel.categoriesFromRoom.collectAsState(initial = listOf()).value
    val taskItemsList = startViewModel.tasksFromRoom.collectAsState(initial = mutableListOf()).value.toMutableStateList()
    val selectedCategory = startViewModel.selectedCategory.collectAsState(initial = -1L).value
    val startScreenUiState: StartScreenState = startViewModel.startScreenUiState.value
    val newTaskState by startViewModel.newTaskState.collectAsState()
    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = true, block = {
        if (selectedFromNav != selectedCategory) {
           startViewModel.setSelectedCategory(selectedFromNav)
        }
    })
    BackHandler(enabled = true) {
        if(startScreenUiState==StartScreenState.AddTask) {
            startViewModel.setStartScreenUiState(StartScreenState.Welcome)
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
                    modifier = Modifier
                        .alpha(0.6f)
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
                            ).asString()
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
                onClick = { itemId -> startViewModel.setSelectedCategory(itemId); },
                startScreenUiState = startScreenUiState,
                navigateTo = { route -> navigateTo(route) }
            )
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
                    closeKeyboard = { keyboardController?.hide() },
                    verifyForm = {
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

                                startViewModel.setStartScreenUiState(StartScreenState.Welcome)
                                startViewModel.onNewTaskNameChange("")
                                startViewModel.onNewTaskDescChange("")
                                startViewModel.getTaskItemsFromRoom(selectedCategory)

                            }
                            // error
                            formDataState.error?.let {
                                //snack message with error
                                snackMessage(it)

                            }

                        }.launchIn(scope)

                    }
                )
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
                    onClick = { itemId -> startViewModel.setSelectedCategory(itemId); },
                    startScreenUiState = startScreenUiState,
                    navigateTo = { route -> navigateTo(route) }
                )
            }
            //Add task form
            AddTaskForm(
                startScreenUiState = startScreenUiState,
                isDarkModeOn = isDarkModeOn,
                categoryId = selectedCategory,
                newTaskName = newTaskState.taskName,
                newTaskDesc = newTaskState.taskDesc,
                onTaskNameChange = startViewModel::onNewTaskNameChange,
                onTaskDescChange = startViewModel::onNewTaskDescChange,
                hideKeyboard = { keyboardController?.hide(); focusManager.clearFocus(true) }

            )
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

            //Category anim to selected category
            if (startViewModel.getOneFromCategoryList(selectedCategory).dCatId != null) {
                LaunchedEffect(key1 = true, block = {
                    startViewModel.selectedCategoryRowIndex.value = categoryList.indexOf(
                        categoryList.find {
                            it.dCatId == selectedCategory
                        }
                    )
                    categoryRowState.animateScrollToItem(startViewModel.selectedCategoryRowIndex.value)
                })
            }

            //Task list
            TasksColumnLazy(
                isDarkModeOn = isDarkModeOn,
                itemColumnState = itemColumnState,
                tasksList = taskItemsList.toMutableStateList(),
                onClickEdit = { },
                onClickCheck = { itemToken: String, newVal: Boolean ->


                    val itemToChangeIndex = taskItemsList.indexOf(taskItemsList.find {
                        it.dItemToken == itemToken
                    })
                    scope.launch {

                        if (startViewModel.updateTaskItemCompletion(
                                taskItemsList[itemToChangeIndex],
                                selectedCategory
                            )
                        ) {
                            taskItemsList[itemToChangeIndex] =
                                taskItemsList[itemToChangeIndex].copy(dItemCompleted = newVal)
                        }

                    }


                },
                startScreenUiState = startScreenUiState,
            )

        }
    }

    }

}