package com.arturlasok.maintodo.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.domain.model.ItemToDo
import java.time.LocalDate

@Composable
fun DateInfo(itemBefore: ItemToDo,sizeCircle: Dp, isDarkModeOn: Boolean, completed:Boolean, date: Long, days: List<String>) {

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