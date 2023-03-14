package com.arturlasok.maintodo.ui.edittask_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.admob.AdMobBigBanner
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.ui.start_screen.*
import com.arturlasok.maintodo.util.*
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditTaskScreen(
                   addAlarm:(time: Long,beganTime: Long, name: String,desc:String, token: String, id: Long) -> Unit,
                   navigateTo: (route: String) -> Unit,
                   isDarkModeOn: Boolean,
                   snackMessage: (snackMessage:String) -> Unit,
                   editTaskViewModel: EditTaskViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val saveButtonEnabled = rememberSaveable { mutableStateOf(true) }
    val state by editTaskViewModel.editItemState.collectAsState()
    val newDateTimeState = editTaskViewModel.editDateTimeState.collectAsState().value
    val localTimeNow = remember {
        mutableStateOf( millisToHour(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong()) )
    }
    val localTimeNowLong = remember {
        mutableStateOf(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong() )
    }
    val dialogStateDate = rememberMaterialDialogState()
    val dialogStateTime = rememberMaterialDialogState()
    val dialogStateNotDate = rememberMaterialDialogState()
    val dialogStateNotTime = rememberMaterialDialogState()

    BackHandler(enabled = true) {
        navigateTo(Screen.Start.route)
    }
    LaunchedEffect(key1 = true, block = {
        editTaskViewModel.getOneItemFromRoom(itemId = editTaskViewModel.getItemFormDi())
        editTaskViewModel.getCategoriesFromRoom()
    })

    Column {


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
                    onClick = { navigateTo(Screen.Start.route) }
                )
                //
                //Add button
                //
                Button(
                    enabled = saveButtonEnabled.value,
                    onClick = {

                        saveButtonEnabled.value = false
                        editTaskViewModel.onItemNameChange(state.itemName)
                        editTaskViewModel.onItemCategoryChange(state.itemCategory)
                        editTaskViewModel.onItemDescriptionChange(state.itemDescription)
                        keyboardController?.hide()

                        //verify form
                        editTaskViewModel.verifyForm().onEach { formDataState ->
                            // ok
                            formDataState.second.ok?.let {
                                snackMessage(
                                    UiText.StringResource(
                                        R.string.edittask_updated,
                                        "no"
                                    ).asString(editTaskViewModel.getApplication().applicationContext)
                                )
                                addAlarm(
                                    (TimeUnit.DAYS.toMillis(newDateTimeState.notDate))+(newDateTimeState.notTime-3600000),
                                    TimeUnit.DAYS.toMillis(newDateTimeState.taskDate)+(newDateTimeState.taskTime-3600000),
                                    state.itemName,
                                    state.itemDescription,
                                    editTaskViewModel.getLastItemSelected(),
                                    editTaskViewModel.getLastItemIdInRoom(),
                                )
                                //nav to start and last added category
                                navigateTo(Screen.Start.route)
                            }
                            // error
                            formDataState.second.error?.let {
                                //snack message with error
                                snackMessage(it)
                                saveButtonEnabled.value = true
                            }

                        }.launchIn(scope)


                    },
                    modifier = Modifier,
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = UiText.StringResource(
                            R.string.editcategory_save,
                            "no"
                        ).asString().uppercase(),
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(start = 0.dp)
                    )
                }
            }
        }
        //Screen title
        Box(
            Modifier
                .fillMaxWidth()
                .height(48.dp), contentAlignment = Alignment.Center) {

            Text(
                text = UiText.StringResource(
                    R.string.edittask_screen,
                    "no"
                ).asString(),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.primary
            )
        }

    }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TaskNameForm(
            nameValue = state.itemName,
            onValueChange = { text -> editTaskViewModel.onItemNameChange(text) },
            isDarkModeOn = isDarkModeOn,
            onDone = { keyboardController?.hide() }
        )
        CategoryRow(
            isDarkModeOn = isDarkModeOn,
            categoryRowState = rememberLazyListState(),
            categoryList = editTaskViewModel.categoriesFromRoom.collectAsState().value,
            selectedCategory = state.itemCategory,
            onClick = { itemToken ->
                focusManager.clearFocus();
                editTaskViewModel.onItemCategoryChange(itemToken)
            },
            startScreenUiState = StartScreenState.AddTask,
            navigateTo = { },
            numberOfItems = remember{ mutableStateListOf() }
        ) {}
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
        TaskDescForm(
            nameValue = state.itemDescription,
            onValueChange = { text -> editTaskViewModel.onItemDescriptionChange(text) },
            isDarkModeOn = isDarkModeOn,
            onDone = { keyboardController?.hide() }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        //REALIZATION
        Row(modifier = Modifier
            .padding(10.dp, bottom = 4.dp)
            .clickable(onClick = { dialogStateDate.show() }),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DatePicker(
                        taskDataType = true,
                        isDarkModeOn = isDarkModeOn,
                        taskLimitDate = LocalDate.ofEpochDay(1000000L),
                        initialDate = LocalDate.ofEpochDay(newDateTimeState.taskDate),
                        light_img = R.drawable.calendar_light,
                        dark_img = R.drawable.calendar_dark,
                        modifier = Modifier,
                        setDate = {
                            //set new task date!
                                date ->  editTaskViewModel.setNewTaskDate(date)
                        },
                        dialogState = dialogStateDate
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = UiText.StringResource(R.string.task_date,"asd").asString(),
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .height(12.dp)
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .border(1.dp, Color.Black)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .height(12.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = millisToDate(TimeUnit.DAYS.toMillis(newDateTimeState.taskDate)),
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.SemiBold
                        )
                        if(newDateTimeState.taskDate != LocalDate.now().toEpochDay()) {
                            IconButton(onClick = {
                                //reset date to now day
                                editTaskViewModel.setNewTaskDate(LocalDate.now().toEpochDay())

                            }) {
                                Icon(
                                    Icons.Filled.Clear, "Clear",
                                    modifier = Modifier
                                        .padding(start = 0.dp, end = 0.dp),
                                    tint = Color.Gray
                                )

                            }
                        }
                    }
                }
            }


        }
        Row(modifier = Modifier
            .padding(10.dp, bottom = 4.dp)
            .clickable(onClick = {
                //if(newDateTimeState.taskDate!=0L) {
                dialogStateTime.show()
                //}

            })
            .alpha(
                if (newDateTimeState.taskDate != 0L) {
                    1.0f
                } else {
                    1.0f
                }
            )
            ,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TimePicker(
                        isDarkModeOn = isDarkModeOn,
                        //initial task limit time from VModel
                        initialTime = LocalTime.ofNanoOfDay(TimeUnit.MILLISECONDS.toNanos(newDateTimeState.taskTime))
                        ,
                        light_img = R.drawable.time_light,
                        dark_img = R.drawable.time_dark,
                        modifier = Modifier,
                        //set new time
                        setTime = { time ->editTaskViewModel.setNewTaskTime(time) },
                        //date to make range of accepted time
                        acceptedDate = newDateTimeState.taskDate,
                        dialogState = dialogStateTime
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = UiText.StringResource(R.string.task_time,"asd").asString(),
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .height(12.dp)
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .border(1.dp, Color.Black)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .height(12.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = millisToHourOfDay(newDateTimeState.taskTime),
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.SemiBold
                        )
                        if((newDateTimeState.taskTime>localTimeNowLong.value && LocalDate.ofEpochDay(newDateTimeState.taskDate).toEpochDay()== LocalDate.now().toEpochDay())
                            || (LocalDate.ofEpochDay(newDateTimeState.taskDate).toEpochDay()> LocalDate.now().toEpochDay()) && TimeUnit.MILLISECONDS.toMinutes(newDateTimeState.taskTime)!= TimeUnit.MILLISECONDS.toMinutes(localTimeNowLong.value)){
                            IconButton(onClick = { editTaskViewModel.setNewTaskTime(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong()) }) {
                                Icon(
                                    Icons.Filled.Clear, "Clear",
                                    modifier = Modifier
                                        .padding(start = 0.dp, end = 0.dp),
                                    tint = Color.Gray
                                )

                            }
                        }
                    }
                }
            }
        }
        //REMINDER
        Row(
            modifier = Modifier
                .padding(10.dp, bottom = 4.dp)
                .clickable(onClick = { dialogStateNotDate.show() }),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DatePicker(
                        taskDataType = false,
                        isDarkModeOn = isDarkModeOn,
                        taskLimitDate = LocalDate.ofEpochDay(newDateTimeState.taskDate),
                        initialDate = if(newDateTimeState.notDate!=0L)
                        { LocalDate.ofEpochDay(newDateTimeState.notDate) }
                        else
                        {
                            if(newDateTimeState.taskDate!=0L) {
                                LocalDate.ofEpochDay(newDateTimeState.taskDate)
                            }
                            else
                            {
                                LocalDate.now()
                            }

                        },
                        light_img = R.drawable.alarm_light,
                        dark_img = R.drawable.alarm_dark,
                        modifier = Modifier,
                        setDate = { date -> editTaskViewModel.setNotTaskDate(date)

                        },
                        dialogState = dialogStateNotDate
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = UiText.StringResource(R.string.task_not_date,"asd").asString(),
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .height(12.dp)
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .border(1.dp, Color.Black)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .height(12.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = if (newDateTimeState.notDate == 0L) {
                                UiText.StringResource(R.string.no_date,"asd").asString().uppercase()
                            } else {
                                millisToDate(TimeUnit.DAYS.toMillis(newDateTimeState.notDate))
                            },
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.SemiBold
                        )
                        if(newDateTimeState.notDate!= 0L) {
                            IconButton(onClick = { editTaskViewModel.setNotTaskDate(0L); editTaskViewModel.setNotTaskTime(0L) }) {
                                Icon(
                                    Icons.Filled.Clear, "Clear",
                                    modifier = Modifier
                                        .padding(start = 0.dp, end = 0.dp),
                                    tint = Color.Gray
                                )

                            }
                        }
                    }
                }
            }


        }
        Row(
            modifier = Modifier
                .padding(10.dp, bottom = 4.dp)
                .clickable(onClick = {
                    if (newDateTimeState.notDate != 0L) {

                        dialogStateNotTime.show()

                    }
                })
                .alpha(
                    if (newDateTimeState.notDate != 0L) {
                        1.0f
                    } else {
                        0.2f
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TimePicker(
                        isDarkModeOn = isDarkModeOn,
                        initialTime = if(newDateTimeState.notTime!=0L)
                        {
                            LocalTime.ofNanoOfDay(TimeUnit.MILLISECONDS.toNanos(newDateTimeState.notTime))
                        }
                        else {
                            if(newDateTimeState.taskTime!=0L) {
                                LocalTime.ofNanoOfDay(TimeUnit.MILLISECONDS.toNanos(newDateTimeState.taskTime))
                            }
                            else {
                                LocalTime.now() }
                        },
                        light_img = R.drawable.alarmclock_light,
                        dark_img = R.drawable.alarmclock_dark,
                        modifier = Modifier,
                        setTime = { time -> editTaskViewModel.setNotTaskTime(time)

                        },
                        acceptedDate = newDateTimeState.notDate,
                        dialogState = dialogStateNotTime
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = UiText.StringResource(R.string.task_not_time,"asd").asString(),
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .height(12.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .border(1.dp, Color.Black)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .height(12.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = if(newDateTimeState.notDate!=0L) { millisToHourOfDay(newDateTimeState.notTime) } else
                            {
                                UiText.StringResource(R.string.no_time,"asd").asString().uppercase()
                            },
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.SemiBold
                        )

                    }
                }
            }

        }

        //AdMobBigBanner()
    }

    }
}