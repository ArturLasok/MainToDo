package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.domain.model.ItemToDo

@Composable
fun SingleTaskItem(
    modifier: Modifier,
    item: ItemToDo,
    indexOfItem: Int,
    isDarkModeOn : Boolean,
    checkChange:(newVal:Boolean) -> Unit
) {
    Surface(
        modifier = modifier,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                modifier = Modifier.padding(0.dp),
                checked = item.dItemCompleted,
                onCheckedChange = { checkChange(!item.dItemCompleted) }
            )
            Text(
                text = item.dItemTitle,
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
    Spacer(modifier = modifier
        .fillMaxWidth()
        .height(6.dp))

}