package com.arturlasok.maintodo.util

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.util.MainTimeDate
import com.arturlasok.maintodo.ui.start_screen.StartViewModel
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit

@Composable
fun DateInfoForBox(
    tasksList: List<ItemToDo>,
    taskListSize: Int,
    startViewModel: StartViewModel,
    sizeCircle: Dp,
    isDarkModeOn: Boolean,
    //completed:Boolean,
    //date: Long,
    days: List<String>
) {


    val completed =try { tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemCompleted } catch (e:Exception) { false }
    val nextcompleted =try { tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value+1 }.value ].dItemCompleted } catch (e:Exception) { false }
    val date =
        try {
            tasksList[startViewModel.firstViItemIndex.value].dItemDeliveryTime.let {

                //if (TimeUnit.MILLISECONDS.toDays(it) >= TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis())) {
                    if (LocalDate.parse(millisToDate(it)).toEpochDay() >=  LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay()) {
it
                   // TimeUnit.DAYS.toMillis(TimeUnit.MILLISECONDS.toDays(it))
                } else {
                    MainTimeDate.systemCurrentTimeInMillis()

                }
            }
        } catch (e: Exception) {
            MainTimeDate.systemCurrentTimeInMillis()
        }

    Log.i(TAG,"DATEBOX RECOMPOSE ${millisToDateAndHour(date)}")
    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 4.dp, top = 6.dp)
            .alpha(0.8f)
            .zIndex(0.9f), contentAlignment = Alignment.TopStart) {
        if(startViewModel.firstViItemIndex.value<=taskListSize) {
/*
            if(taskListSize>1 && (tasksList.count { TimeUnit.MILLISECONDS.toDays(it.dItemDeliveryTime) <= TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis())  }>1 || tasksList.count { TimeUnit.MILLISECONDS.toDays(it.dItemDeliveryTime) > TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis())  }>1) &&

                (
                        (TimeUnit.MILLISECONDS.toDays(startViewModel.actualItem.value.dItemDeliveryTime)<= TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis()) && TimeUnit.MILLISECONDS.toDays(startViewModel.nextItem.value.dItemDeliveryTime)<= TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis()))
                                ||
                                //(TimeUnit.MILLISECONDS.toDays(actualItem.dItemDeliveryTime)>TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) && TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value].dItemDeliveryTime)!=TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value-1].dItemDeliveryTime  )&& TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value].dItemDeliveryTime)==TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value+1].dItemDeliveryTime  ))
                                (TimeUnit.MILLISECONDS.toDays(startViewModel.actualItem.value.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis()) && TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)!= TimeUnit.MILLISECONDS.toDays(startViewModel.prevItem.value.dItemDeliveryTime)&& TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)== TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value +1].dItemDeliveryTime  ))
                                ||
                                (TimeUnit.MILLISECONDS.toDays(startViewModel.actualItem.value.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis()) && TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)== TimeUnit.MILLISECONDS.toDays(startViewModel.prevItem.value.dItemDeliveryTime)&& TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)== TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value +1].dItemDeliveryTime  ))
                        )
            ) {


            val reason1 = taskListSize>1
            val reason2 = (tasksList.count { LocalDate.parse(millisToDate(it.dItemDeliveryTime)).toEpochDay() <= LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay() }>1 || tasksList.count { LocalDate.parse(millisToDate(it.dItemDeliveryTime)).toEpochDay() > LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay() }>1)
            val reason3_1 = (LocalDate.parse(millisToDate(startViewModel.actualItem.value.dItemDeliveryTime)).toEpochDay()<=LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay()  && LocalDate.parse(millisToDate(startViewModel.nextItem.value.dItemDeliveryTime)).toEpochDay()<= LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay())
            val reason3_2 =   (LocalDate.parse(millisToDate(startViewModel.actualItem.value.dItemDeliveryTime)).toEpochDay()> LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay() && LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)).toEpochDay()!= LocalDate.parse(millisToDate(startViewModel.prevItem.value.dItemDeliveryTime)).toEpochDay()&& LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)).toEpochDay()==  LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value +1].dItemDeliveryTime)).toEpochDay() )
            val reason3_3 =   (LocalDate.parse(millisToDate(startViewModel.actualItem.value.dItemDeliveryTime)).toEpochDay()> LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay() && LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)).toEpochDay()== LocalDate.parse(millisToDate(startViewModel.prevItem.value.dItemDeliveryTime)).toEpochDay()&& LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)).toEpochDay()== LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value +1].dItemDeliveryTime  )).toEpochDay())
            Log.i(TAG,"DATEINFO 1: ${reason1}")
            Log.i(TAG,"DATEINFO 2: ${reason2}")
            Log.i(TAG,"DATEINFO 3-1: ${reason3_1}")
            Log.i(TAG,"DATEINFO 3-2: ${reason3_2}")
            Log.i(TAG,"DATEINFO 3-3: ${reason3_3}")
            Log.i(TAG,"DATEINFO actual item to epoch: ${LocalDate.parse(millisToDate(startViewModel.actualItem.value.dItemDeliveryTime)).toEpochDay()} // ${startViewModel.actualItem.value.dItemTitle}")
            Log.i(TAG,"DATEINFO next item to epoch: ${LocalDate.parse(millisToDate(startViewModel.nextItem.value.dItemDeliveryTime)).toEpochDay()} // ${startViewModel.nextItem.value.dItemTitle}")
            Log.i(TAG,"DATEINFO system epoch: ${LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay()}")


            */
        if(taskListSize>1 && (tasksList.count { LocalDate.parse(millisToDate(it.dItemDeliveryTime)).toEpochDay() <= LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay() }>1 || tasksList.count { LocalDate.parse(millisToDate(it.dItemDeliveryTime)).toEpochDay() > LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay() }>1) &&

            (
                    (LocalDate.parse(millisToDate(startViewModel.actualItem.value.dItemDeliveryTime)).toEpochDay()<=LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay()  && LocalDate.parse(millisToDate(startViewModel.nextItem.value.dItemDeliveryTime)).toEpochDay()<= LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay())
                            ||
                            //(TimeUnit.MILLISECONDS.toDays(actualItem.dItemDeliveryTime)>TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) && TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value].dItemDeliveryTime)!=TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value-1].dItemDeliveryTime  )&& TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value].dItemDeliveryTime)==TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value+1].dItemDeliveryTime  ))
                            (LocalDate.parse(millisToDate(startViewModel.actualItem.value.dItemDeliveryTime)).toEpochDay()> LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay() && LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)).toEpochDay()!= LocalDate.parse(millisToDate(startViewModel.prevItem.value.dItemDeliveryTime)).toEpochDay()&& LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)).toEpochDay()==  LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value +1].dItemDeliveryTime)).toEpochDay() )
                            ||
                            (LocalDate.parse(millisToDate(startViewModel.actualItem.value.dItemDeliveryTime)).toEpochDay()> LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay() && LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)).toEpochDay()== LocalDate.parse(millisToDate(startViewModel.prevItem.value.dItemDeliveryTime)).toEpochDay()&& LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)).toEpochDay()== LocalDate.parse(millisToDate(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value +1].dItemDeliveryTime  )).toEpochDay())
                    )
        ) {


    Column(modifier = Modifier
        .width(42.dp)
        .height(60.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            if(!completed && !nextcompleted) {
                Text(
                    text = days[milisToDayOfWeek(date) - 1].substring(0, 3).uppercase(),
                    style = MaterialTheme.typography.h4

                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(sizeCircle)
                        .clip(CircleShape)
                        .background(
                            if (LocalDate.now().dayOfMonth == millisToDay(date).toInt() && millisToMonth(
                                    date
                                ).toInt() == LocalDate.now().month.value
                            ) {
                                if (isDarkModeOn) {
                                    MaterialTheme.colors.secondary
                                } else {
                                    MaterialTheme.colors.secondary.copy(alpha = 0.5f)
                                }
                            } else {
                                if (isDarkModeOn) {
                                    MaterialTheme.colors.onSecondary
                                } else {
                                    MaterialTheme.colors.secondary.copy(alpha = 0.1f)
                                }
                            }
                        )
                        .clickable(onClick = { })


                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${millisToDay(date)}",
                            style = MaterialTheme.typography.h3,
                            color = if (LocalDate.now().dayOfMonth == millisToDay(date).toInt() && millisToMonth(
                                    date
                                ).toInt() == LocalDate.now().month.value
                            ) {
                                if (isDarkModeOn) {
                                    MaterialTheme.colors.background
                                } else {
                                    Color.White
                                }
                            } else {
                                if (isDarkModeOn) {
                                    MaterialTheme.colors.primary
                                } else {
                                    MaterialTheme.colors.primary
                                }
                            }

                        )
                        Spacer(
                            modifier = Modifier
                                .width(20.dp)
                                .height(1.dp)
                                .background(
                                    if (LocalDate.now().dayOfMonth == millisToDay(date).toInt() && millisToMonth(
                                            date
                                        ).toInt() == LocalDate.now().month.value
                                    ) {
                                        if (isDarkModeOn) {
                                            MaterialTheme.colors.background
                                        } else {
                                            Color.White
                                        }
                                    } else {
                                        if (isDarkModeOn) {
                                            MaterialTheme.colors.primary
                                        } else {
                                            MaterialTheme.colors.primary
                                        }
                                    }
                                )
                        )
                        Text(
                            text = millisToMonth(date),
                            style = MaterialTheme.typography.h5,
                            color = if (LocalDate.now().dayOfMonth == millisToDay(date).toInt() && millisToMonth(
                                    date
                                ).toInt() == LocalDate.now().month.value
                            ) {
                                if (isDarkModeOn) {
                                    MaterialTheme.colors.background
                                } else {
                                    Color.White
                                }
                            } else {
                                if (isDarkModeOn) {
                                    MaterialTheme.colors.primary
                                } else {
                                    MaterialTheme.colors.primary
                                }
                            }

                        )
                    }
                }


            }
    }
}
        }
    }
}