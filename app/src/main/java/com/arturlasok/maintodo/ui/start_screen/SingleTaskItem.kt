package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import com.arturlasok.maintodo.interactors.util.MainTimeDate
import com.arturlasok.maintodo.util.*
import java.util.concurrent.TimeUnit

@Composable
fun SingleTaskItem(
    firstVisibleIndexX: State<MutableState<Int>>,
    itemColumnState: LazyListState,
    modifier: Modifier,
    itemBefore: ItemToDo,
    itemNext: ItemToDo,
    item: ItemToDo,
    confirmationTaskSetting: Boolean,
    indexOfItem: Int,
    isDarkModeOn: Boolean,
    checkChange:(newVal:Boolean) -> Unit,
    deleteClick:(itemId: Long) -> Unit,
    editClick:(itemToken: String) -> Unit,
    removeAll:() ->Unit,
    fullOpen:Boolean,
    fviChange:(id:Int) ->Unit,
    setOpen:(id:Long) ->Unit,

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
    Column() {


    RemoveAllTasks(
        isDarkModeOn = isDarkModeOn,
        itemBefore = itemBefore,
        itemNext = itemNext,
        item = item,
        modifier = Modifier,
        removeAll = { removeAll()}
    )
    Row(modifier = Modifier.fillMaxWidth()) {

        Column(modifier = Modifier
            .width(42.dp)
            , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            SingleTaskDateInfo(
                item = item,
                itemBefore = itemBefore,
                itemNext = itemNext,
                firstVisibleIndexX = firstVisibleIndexX,
                indexOfItem = indexOfItem,
                isDarkModeOn = isDarkModeOn,

            )

        }
        Column() {




        Surface(
            modifier = modifier.clickable(onClick = { item.dItemId?.let { if(!item.dItemCompleted) { setOpen(it) } } }),
            shape = MaterialTheme.shapes.medium,
            elevation = 2.dp,

            color = if (isDarkModeOn) {
                if (item.dItemCompleted) {
                    MaterialTheme.colors.onSecondary
                } else {
                    MaterialTheme.colors.primaryVariant
                }

            } else {
                if (item.dItemCompleted) {
                    Color.LightGray
                } else {
                    MaterialTheme.colors.primaryVariant
                }
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

                                if (confirmationTaskSetting) {
                                    changeTaskStatusAlert.value = true
                                } else {
                                    checkChange(!item.dItemCompleted)

                                }

                            },
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = Color.LightGray
                            )
                        )

                        Column() {

                            Row() {

                                Text(
                                    //maxLines = if (fullOpen) 2 else 1,
                                    text = if (item.dItemDeliveryTime != 0L && !fullOpen && TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)>=TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis())) {
                                        "${MainTimeDate.localFormTime(item.dItemDeliveryTime)} "
                                    } else {
                                       ""
                                    },
                                    style = MaterialTheme.typography.h3,
                                    fontWeight = FontWeight.Normal,
                                    textDecoration = if (item.dItemCompleted) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    },
                                    color = if (isDarkModeOn) {
                                        Color.White

                                    } else {
                                        if (item.dItemCompleted) {
                                            MaterialTheme.colors.primary
                                        } else {
                                            Color.White
                                        }
                                    }
                                )
                                Text(
                                    maxLines = if (fullOpen) 2 else 1,
                                    overflow = TextOverflow.Ellipsis,
                                    text =  if (fullOpen) { if (item.dItemDeliveryTime != 0L) {
                                        if(TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)>=TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis())) {
                                        "${MainTimeDate.localFormTime(item.dItemDeliveryTime)} "+ item.dItemTitle} else { item.dItemTitle}


                                    } else {
                                        ""
                                    } } else { item.dItemTitle},
                                    style = MaterialTheme.typography.h3,
                                    fontWeight = FontWeight.Medium,
                                    textDecoration = if (item.dItemCompleted) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    },
                                    color = if (isDarkModeOn) {
                                        Color.White

                                    } else {
                                        if (item.dItemCompleted) {
                                            MaterialTheme.colors.primary
                                        } else {
                                            Color.White
                                        }
                                    }
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .fillMaxWidth(0.9f)
                            ) {


                                if (item.dItemRemindTime > 0L && TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)>=TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis())) {
                                    Row(Modifier.fillMaxWidth()) {
                                        //Spacer(modifier = Modifier.width(12.dp))
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
                                                    Color.White

                                                } else {
                                                    if (item.dItemCompleted) {
                                                        MaterialTheme.colors.primary
                                                    } else {
                                                        MaterialTheme.colors.onSecondary
                                                    }
                                                }
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            text = if (item.dItemRemindTime > 0L) {
                                                MainTimeDate.localFormDateAndTime(item.dItemRemindTime)
                                            } else {
                                                ""
                                            },
                                            style = MaterialTheme.typography.h5,
                                            fontWeight = FontWeight.Normal,
                                            textDecoration = if (item.dItemCompleted || item.dItemRemindTime<MainTimeDate.systemCurrentTimeInMillis()) {
                                                TextDecoration.LineThrough
                                            } else {
                                                TextDecoration.None
                                            },
                                            color = if (isDarkModeOn) {
                                                Color.White

                                            } else {
                                                if (item.dItemCompleted) {
                                                    MaterialTheme.colors.primary
                                                } else {
                                                    MaterialTheme.colors.onSecondary
                                                }
                                            }
                                        )
                                    }
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
                                onClick = { item.dItemId?.let { if(!item.dItemCompleted) { setOpen(it) } } },
                                light_img = R.drawable.more,
                                dark_img = R.drawable.more
                            )
                        }
                    }

                }
                if (fullOpen) {
                    Row(modifier = Modifier.padding(bottom = 10.dp)) {
                        Column(
                            modifier = Modifier
                                .padding(start = 54.dp, end = 10.dp, bottom = 10.dp)
                                .fillMaxWidth(0.85f)
                        ) {
                            Text(
                                text = if (item.dItemDescription.isNotEmpty()) {
                                    item.dItemDescription
                                } else {
                                    UiText.StringResource(
                                        R.string.no_disc,
                                        "asd"
                                    ).asString() + "\n"
                                },
                                style = MaterialTheme.typography.h4,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Justify,
                                color = if (isDarkModeOn) {
                                    Color.White

                                } else {
                                    if (item.dItemCompleted) {
                                        MaterialTheme.colors.primary
                                    } else {
                                        Color.White
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
                                tintLight = ColorFilter.tint(Color.White),
                                tintDark = ColorFilter.tint(Color.White),
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
        }
        }
    }
    Spacer(modifier = modifier
        .fillMaxWidth()
        .height(6.dp))

}