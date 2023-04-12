package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.util.TrashButton
import com.arturlasok.maintodo.util.UiText

@Composable
fun RemoveAllTasks(
    isDarkModeOn: Boolean,
    itemBefore: ItemToDo,
    itemNext: ItemToDo,
    modifier: Modifier,
    item: ItemToDo,
    removeAll:() -> Unit
) {
    if(!itemBefore.dItemCompleted && item.dItemCompleted) {
        Column(modifier = Modifier.fillMaxWidth().padding(start = 42.dp), horizontalAlignment = Alignment.Start) {
            Surface(
                modifier = Modifier.fillMaxWidth().clickable(onClick = { removeAll() })
                    .background(
                        if (!isDarkModeOn) {
                            Color.White
                        } else {
                            MaterialTheme.colors.onSecondary
                        }, MaterialTheme.shapes.medium
                    ),
                shape = MaterialTheme.shapes.medium,
                elevation = 4.dp,
                border = BorderStroke(1.dp, Color.DarkGray),
                color = if (!isDarkModeOn) {
                    Color.White
                } else {
                    MaterialTheme.colors.onSecondary
                }
            )
            {
                Row(
                    modifier = Modifier.padding(6.dp).padding(end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) { Text(text = " "+  UiText.StringResource(
                    R.string.removeallcompleted,
                    "no"
                ).asString(), style = MaterialTheme.typography.h3)
                    TrashButton(
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier,
                        onClick = { removeAll() }
                    )

                }
            }
        }
    }
}