package com.arturlasok.maintodo.util

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.ui.start_screen.StartViewModel
import java.time.LocalDate
import java.util.concurrent.TimeUnit

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

    Log.i(TAG,"DATEBOX RECOMPOSE")
    val completed =try { tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemCompleted }
    catch (e:Exception) { false }
    val date =
        try {
            tasksList[startViewModel.firstViItemIndex.value].dItemDeliveryTime.let {

                if (TimeUnit.MILLISECONDS.toDays(it) >= TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())) {

                    it
                } else {
                    System.currentTimeMillis()

                }
            }
        } catch (e: Exception) {
             System.currentTimeMillis()
        }


    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 4.dp, top = 4.dp)
            .alpha(0.8f)
            .zIndex(0.9f), contentAlignment = Alignment.TopStart) {
        if(startViewModel.firstViItemIndex.value<=taskListSize) {

        if(taskListSize>1 &&

            (
                    (TimeUnit.MILLISECONDS.toDays(startViewModel.actualItem.value.dItemDeliveryTime)<= TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) && TimeUnit.MILLISECONDS.toDays(startViewModel.nextItem.value.dItemDeliveryTime)<= TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()))
                            ||
                            //(TimeUnit.MILLISECONDS.toDays(actualItem.dItemDeliveryTime)>TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) && TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value].dItemDeliveryTime)!=TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value-1].dItemDeliveryTime  )&& TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value].dItemDeliveryTime)==TimeUnit.MILLISECONDS.toDays(tasksList.sortedBy { it.dItemDeliveryTime }.sortedBy { it.dItemCompleted }[firstViItemIndex.value+1].dItemDeliveryTime  ))
                            (TimeUnit.MILLISECONDS.toDays(startViewModel.actualItem.value.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) && TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)!= TimeUnit.MILLISECONDS.toDays(startViewModel.prevItem.value.dItemDeliveryTime)&& TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)== TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value +1].dItemDeliveryTime  ))
                            ||
                            (TimeUnit.MILLISECONDS.toDays(startViewModel.actualItem.value.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) && TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)== TimeUnit.MILLISECONDS.toDays(startViewModel.prevItem.value.dItemDeliveryTime)&& TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value ].dItemDeliveryTime)== TimeUnit.MILLISECONDS.toDays(tasksList[derivedStateOf {  startViewModel.firstViItemIndex.value }.value +1].dItemDeliveryTime  ))
                    )
        ) {
    Column(modifier = Modifier
        .width(42.dp)
        .height(60.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            if(!completed) {
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