package com.arturlasok.maintodo.ui.start_screen

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.util.RemoveAlert
import com.arturlasok.maintodo.util.TAG
import com.arturlasok.maintodo.util.TrashButton
import com.arturlasok.maintodo.util.UiText

@Composable
fun SingleTaskItem(
    modifier: Modifier,
    item: ItemToDo,
    confirmationTaskSetting: Boolean,
    indexOfItem: Int,
    isDarkModeOn : Boolean,
    checkChange:(newVal:Boolean) -> Unit,
    deleteClick:(itemId: Long) -> Unit,
    editClick:(itemId: Long) -> Unit,
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
                    Text(
                        text = item.dItemTitle,
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if(item.dItemCompleted) { TextDecoration.LineThrough} else {TextDecoration.None},
                        color = if(isDarkModeOn) {
                            Color.DarkGray

                        } else {
                            if(item.dItemCompleted) {
                            MaterialTheme.colors.primary }
                            else { Color.DarkGray }
                        }
                    )
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
                        SettingsButton(
                            isDarkModeOn = isDarkModeOn,
                            modifier = Modifier
                                .padding(2.dp)
                                .alpha(0.5f),
                            light_img = R.drawable.edit_light,
                            dark_img = R.drawable.edit_light,
                            onClick = { item.dItemId?.let { editClick(it) } }
                        )
                    }
                }

            }
            if(fullOpen && item.dItemDescription.isNotEmpty()) {
                Column(modifier = Modifier.padding(start = 54.dp, end = 10.dp, bottom = 10.dp)) {
                    Text(
                        text = item.dItemDescription,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
    Spacer(modifier = modifier
        .fillMaxWidth()
        .height(6.dp))

}