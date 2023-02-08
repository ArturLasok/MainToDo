package com.arturlasok.maintodo.ui.start_screen


import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.util.*
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Date
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddTaskForm(
    newDateTimeState: NewDateTimeState,
    onNewDateChange:(date: Long) -> Unit,
    onNewTimeChange:(time: Long) -> Unit,
    onNewNotDateChange:(date: Long) -> Unit,
    onNewNotTimeChange:(time: Long) -> Unit,
    onNewErrorDateTimeSet:(error: Int) -> Unit,
    startScreenUiState: StartScreenState,
    isDarkModeOn: Boolean,
    categoryId: String,
    newTaskName: String,
    newTaskDesc:String,
    onTaskNameChange:(text: String) -> Unit,
    onTaskDescChange:(text: String) -> Unit,
    hideKeyboard:() ->Unit,

    ) {
    val dialogStateDate = rememberMaterialDialogState()
    val dialogStateTime = rememberMaterialDialogState()
    val dialogStateNotDate = rememberMaterialDialogState()
    val dialogStateNotTime = rememberMaterialDialogState()
    val localVisible = rememberSaveable{ mutableStateOf(false) }
    LaunchedEffect(key1 = startScreenUiState, block = {

        if(localVisible.value) {
            localVisible.value = startScreenUiState == StartScreenState.AddTask
        } else {
            delay(700)
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
            //REALIZATION
            Row(modifier = Modifier.padding(10.dp, bottom = 4.dp).clickable(onClick = { dialogStateDate.show() }),
                verticalAlignment = Alignment.CenterVertically

                ) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DatePicker(
                            taskDataType = true,
                            isDarkModeOn = isDarkModeOn,
                            taskLimitDate = LocalDate.ofEpochDay(1000000L),
                            //initial date from VModel
                            initialDate = if(newDateTimeState.taskDate!=0L)
                            { LocalDate.ofEpochDay(newDateTimeState.taskDate) }
                            else
                            { LocalDate.now() },
                            light_img = R.drawable.calendar_light,
                            dark_img = R.drawable.calendar_dark,
                            modifier = Modifier,
                            setDate = {
                            //set new task date!
                            date ->  onNewDateChange(date)

                                //if time of new task is older then time now it is reset to null 0L
                                if(date== LocalDate.now().toEpochDay()) {

                                    if(newDateTimeState.taskTime.toInt()<LocalTime.now().toKotlinLocalTime().toMillisecondOfDay()) {
                                        onNewTimeChange(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong())
                                    }

                                }
                                //if date of notification is after new task date is not reset to date of task
                                if(LocalDate.ofEpochDay(date)<LocalDate.ofEpochDay(newDateTimeState.notDate))   {
                                    onNewNotDateChange(date)

                                    if(newDateTimeState.taskTime.toInt()<newDateTimeState.notTime.toInt()) {
                                        onNewNotTimeChange(newDateTimeState.taskTime)
                                    }
                                }
                                //if date of notification is after new task date is not reset to date of task
                                if(LocalDate.ofEpochDay(date)==LocalDate.ofEpochDay(newDateTimeState.notDate))   {


                                    if(newDateTimeState.taskTime<newDateTimeState.notTime && (newDateTimeState.taskTime!=0L)) {
                                        onNewNotTimeChange(newDateTimeState.taskTime)
                                    }
                                }


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
                            modifier = Modifier.height(48.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = if (newDateTimeState.taskDate == 0L) {
                                    UiText.StringResource(R.string.no_date,"asd").asString().uppercase()
                                } else {
                                    millisToDate(TimeUnit.DAYS.toMillis(newDateTimeState.taskDate))

                                },
                                style = MaterialTheme.typography.h3,
                                fontWeight = FontWeight.SemiBold
                            )
                            if(newDateTimeState.taskDate != 0L) {
                                IconButton(onClick = { onNewDateChange(0L) }) {
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
            Row(modifier = Modifier.padding(10.dp, bottom = 4.dp).clickable(onClick = {
                if(newDateTimeState.taskDate!=0L) {
                    dialogStateTime.show()
                }

            }).alpha(if(newDateTimeState.taskDate!=0L) { 1.0f } else { 0.2f})
            ,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TimePicker(
                            isDarkModeOn = isDarkModeOn,
                            //initial task limit time from VModel
                            initialTime = if(newDateTimeState.taskTime!=0L)
                            {
                                LocalTime.ofNanoOfDay(TimeUnit.MILLISECONDS.toNanos(newDateTimeState.taskTime))

                            }
                            else {
                                 LocalTime.now()
                                 },
                            light_img = R.drawable.time_light,
                            dark_img = R.drawable.time_dark,
                            modifier = Modifier,
                            //set new time
                            setTime = { time ->onNewTimeChange(time)
                                //if date of notification is after new task date is not reset to date of task
                                if(LocalDate.ofEpochDay(newDateTimeState.taskDate)==LocalDate.ofEpochDay(newDateTimeState.notDate))   {


                                    if(time<newDateTimeState.notTime && (newDateTimeState.taskTime!=0L)) {
                                        onNewNotTimeChange(time)
                                    }
                                }
                                      },
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
                            modifier = Modifier.height(48.dp).fillMaxWidth()
                            ) {
                            Text(
                                text = if (newDateTimeState.taskTime == 0L) {
                                    UiText.StringResource(R.string.no_time,"asd").asString().uppercase()
                                } else {
                                    millisToHour(newDateTimeState.taskTime)
                                },
                                style = MaterialTheme.typography.h3,
                                fontWeight = FontWeight.SemiBold
                            )
                            if(newDateTimeState.taskTime != 0L) {
                                IconButton(onClick = { onNewTimeChange(0L) }) {
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
                    modifier = Modifier.padding(10.dp, bottom = 4.dp).clickable(onClick = { dialogStateNotDate.show() }),
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
                                setDate = { date -> onNewNotDateChange(date)

                                    if(date== LocalDate.now().toEpochDay()) {

                                        if(newDateTimeState.notTime.toInt()<LocalTime.now().toKotlinLocalTime().toMillisecondOfDay()) {
                                            onNewNotTimeChange(LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong())
                                        }


                                    }
                                    //if date of notification is after new task date is not reset to date of task
                                    if(LocalDate.ofEpochDay(newDateTimeState.taskDate)==LocalDate.ofEpochDay(date))   {


                                        if(newDateTimeState.taskTime<newDateTimeState.notTime && (newDateTimeState.taskTime!=0L)) {
                                            onNewNotTimeChange(newDateTimeState.taskTime)
                                        }
                                    }


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
                                modifier = Modifier.height(48.dp).fillMaxWidth()
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
                                    IconButton(onClick = { onNewNotDateChange(0L) }) {
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
                    modifier = Modifier.padding(10.dp, bottom = 4.dp).clickable(onClick = {
                       if(newDateTimeState.notDate!=0L) {

                           dialogStateNotTime.show()

                       }
                    }).alpha(if(newDateTimeState.notDate!=0L) { 1.0f } else { 0.2f}),
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
                                setTime = { time -> onNewNotTimeChange(time)
                                    //if date of notification is after new task date is not reset to date of task
                                    if(LocalDate.ofEpochDay(newDateTimeState.taskDate)==LocalDate.ofEpochDay(newDateTimeState.notDate))   {


                                        if(newDateTimeState.taskTime<time && (newDateTimeState.taskTime!=0L)) {
                                            onNewNotTimeChange(newDateTimeState.taskTime)
                                        }
                                    }
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
                                modifier = Modifier.height(48.dp).fillMaxWidth()
                            ) {
                                Text(
                                    text = if (newDateTimeState.notTime == 0L) {
                                        UiText.StringResource(R.string.no_time,"asd").asString().uppercase()
                                    } else {
                                        millisToHour(newDateTimeState.notTime)
                                    },
                                    style = MaterialTheme.typography.h3,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if(newDateTimeState.notTime != 0L) {
                                    IconButton(onClick = { onNewNotTimeChange(0L) }) {
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

            /*
            Text("NewDate in milis: ${TimeUnit.DAYS.toMillis(newDateTimeState.taskDate)}")
            Text("NewTime in milis: ${newDateTimeState.taskTime}")
            Text("NewDate is: ${millisToDateAndHour(TimeUnit.DAYS.toMillis(newDateTimeState.taskDate)+newDateTimeState.taskTime)}")
            Spacer(modifier = Modifier.height(10.dp))
            Text("notDate in milis: ${TimeUnit.DAYS.toMillis(newDateTimeState.notDate)}")
            Text("motTime in milis: ${newDateTimeState.notTime}")
            Text("noDate is: ${millisToDateAndHour(TimeUnit.DAYS.toMillis(newDateTimeState.notDate)+newDateTimeState.notTime)}")


             */
        //AdMobBigBanner()
        }
    }
}