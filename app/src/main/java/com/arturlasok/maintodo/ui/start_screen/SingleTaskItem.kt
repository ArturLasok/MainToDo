package com.arturlasok.maintodo.ui.start_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.util.*

@Composable
fun SingleTaskItem(
    modifier: Modifier,
    item: ItemToDo,
    confirmationTaskSetting: Boolean,
    indexOfItem: Int,
    isDarkModeOn : Boolean,
    checkChange:(newVal:Boolean) -> Unit,
    deleteClick:(itemId: Long) -> Unit,
    editClick:(itemToken: String) -> Unit,
    fullOpen:Boolean,
    setOpen:(id:Long) ->Unit
) {
    val changeTaskStatusAlert = rememberSaveable { mutableStateOf(false) }
    //Task ready alert!
    if(changeTaskStatusAlert.value) {
        RemoveAlert(
            question =  if(item.dItemCompleted) { UiText.StringResource(
                R.string.task_status_change_question_two,
                "no"
            ).asString()} else {UiText.StringResource(
                R.string.task_status_change_question,
                "no"
            ).asString() },
            onYes = {
                checkChange(!item.dItemCompleted)
                changeTaskStatusAlert.value =false

            },
            onCancel = {
                //cancel
                changeTaskStatusAlert.value = false })
        {
            //on dismiss
            changeTaskStatusAlert.value =false
        }
    }
    Surface(
        modifier = modifier.clickable(onClick = { item.dItemId?.let { setOpen(it) } }),
        shape = MaterialTheme.shapes.medium,
        elevation = 2.dp,

        color = if (isDarkModeOn) {
            if(item.dItemCompleted) { MaterialTheme.colors.onSecondary } else {
            MaterialTheme.colors.primaryVariant }

        } else {
            if(item.dItemCompleted) { Color.LightGray} else {
            MaterialTheme.colors.primaryVariant }
        }


    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Checkbox(
                        modifier = Modifier.padding(0.dp),
                        checked = item.dItemCompleted,
                        onCheckedChange = {

                            if(confirmationTaskSetting) {
                                changeTaskStatusAlert.value = true
                            } else {
                            checkChange(!item.dItemCompleted) }

                                          },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = Color.Gray
                        )
                    )
                    Column() {


                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = item.dItemTitle,
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (item.dItemCompleted) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            },
                            color = if (isDarkModeOn) {
                                Color.DarkGray

                            } else {
                                if (item.dItemCompleted) {
                                    MaterialTheme.colors.primary
                                } else {
                                    Color.Black
                                }
                            }
                        )
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(0.9f)
                        ) {
                            Row() {
                                Image(
                                    bitmap = ImageBitmap.imageResource(
                                        id = R.drawable.calendar_dark
                                    ),
                                    modifier = modifier
                                        .size(
                                            16.dp,
                                            16.dp
                                        ).alpha(
                                            if (item.dItemDeliveryTime != 0L) {
                                                1.0f
                                            } else {
                                                0.2f
                                            }
                                        ),
                                    contentDescription = "Pick Date",
                                    colorFilter = ColorFilter.tint(
                                        if (isDarkModeOn) {
                                            Color.DarkGray

                                        } else {
                                            if (item.dItemCompleted) {
                                                MaterialTheme.colors.primary
                                            } else {
                                                Color.DarkGray
                                            }
                                        }
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    modifier = Modifier.alpha(
                                        if (item.dItemDeliveryTime != 0L) {
                                            1.0f
                                        } else {
                                            0.2f
                                        }
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    text = if (item.dItemDeliveryTime != 0L) {
                                        millisToDateAndHour(item.dItemDeliveryTime)
                                    } else {
                                        "-"
                                    },
                                    style = MaterialTheme.typography.h5,
                                    fontWeight = FontWeight.Normal,
                                    textDecoration = if (item.dItemCompleted) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    },
                                    color = if (isDarkModeOn) {
                                        Color.DarkGray

                                    } else {
                                        if (item.dItemCompleted) {
                                            MaterialTheme.colors.primary
                                        } else {
                                            Color.DarkGray
                                        }
                                    }
                                )
                            }
                            Row() {
                                Spacer(modifier = Modifier.width(12.dp))
                                Image(
                                    bitmap = ImageBitmap.imageResource(
                                        id = R.drawable.alarmclock_dark
                                    ),
                                    modifier = modifier
                                        .size(
                                            16.dp,
                                            16.dp
                                        ),
                                    contentDescription = "Pick Date",
                                    colorFilter = ColorFilter.tint(
                                        if (isDarkModeOn) {
                                            Color.DarkGray

                                        } else {
                                            if (item.dItemCompleted) {
                                                MaterialTheme.colors.primary
                                            } else {
                                                Color.DarkGray
                                            }
                                        }
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    text = if (item.dItemRemindTime != 0L) {
                                        millisToDateAndHour(item.dItemRemindTime)
                                    } else {
                                        "-"
                                    },
                                    style = MaterialTheme.typography.h5,
                                    fontWeight = FontWeight.Normal,
                                    textDecoration = if (item.dItemCompleted) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    },
                                    color = if (isDarkModeOn) {
                                        Color.DarkGray

                                    } else {
                                        if (item.dItemCompleted) {
                                            MaterialTheme.colors.primary
                                        } else {
                                            Color.DarkGray
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                if (item.dItemCompleted) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 6.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        TrashButton(
                            isDarkModeOn = isDarkModeOn,
                            modifier = Modifier,
                            onClick = { item.dItemId?.let { deleteClick(it) } }
                        )
                    }

                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 6.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        MoreButton(
                            isDarkModeOn = isDarkModeOn,
                            rotatedDown = fullOpen,
                            modifier = Modifier.alpha(0.5f),
                            onClick = {  item.dItemId?.let { setOpen(it) }},
                            light_img = R.drawable.more,
                            dark_img = R.drawable.more
                        )
                    }
                }

            }
            if(fullOpen) {
                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(start = 54.dp, end = 10.dp, bottom = 10.dp)
                            .fillMaxWidth(0.85f)
                    ) {
                        Text(
                            text = if(item.dItemDescription.isNotEmpty()) {item.dItemDescription } else {
                                UiText.StringResource(
                                    R.string.no_disc,
                                    "asd"
                                ).asString()+"\n"
                                                                                                        },
                            style = MaterialTheme.typography.h4,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Justify,
                            color = if (isDarkModeOn) {
                                Color.DarkGray

                            } else {
                                if (item.dItemCompleted) {
                                    MaterialTheme.colors.primary
                                } else {
                                    Color.DarkGray
                                }
                            }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        SettingsButton(
                            isDarkModeOn = isDarkModeOn,
                            modifier = Modifier
                                .padding(2.dp)
                                .alpha(0.5f),
                            light_img = R.drawable.edit_light,
                            dark_img = R.drawable.edit_light,
                            onClick = { item.dItemToken?.let { editClick(it) } }
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = modifier
        .fillMaxWidth()
        .height(6.dp))

}