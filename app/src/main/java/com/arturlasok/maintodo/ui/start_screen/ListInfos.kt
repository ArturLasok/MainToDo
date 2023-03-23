package com.arturlasok.maintodo.ui.start_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.util.TAG
import com.arturlasok.maintodo.util.millisToMonth
import com.arturlasok.maintodo.util.millisToWeekNumber
import com.arturlasok.maintodo.util.millisToYear

@Composable
fun ListInfos(
    months: List<String>,
    firstVisibleIndexX: State<MutableState<Int>>,
    itemBefore: ItemToDo,
    itemNext: ItemToDo,
    item: ItemToDo,
    modifier: Modifier,

    ) {
    //Log.i(TAG,"item year ${(millisToYear(item.dItemDeliveryTime))} itembefor year${(millisToYear(itemBefore.dItemDeliveryTime))} ")
    Log.i(TAG,"ITEM recompose ${item.dItemTitle}")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,


    ) {
        //Month
        if(((millisToMonth(item.dItemDeliveryTime).toInt()> millisToMonth(itemBefore.dItemDeliveryTime).toInt()) || (millisToYear(item.dItemDeliveryTime)> millisToYear(itemBefore.dItemDeliveryTime)) )
            && !item.dItemCompleted
            && (millisToMonth(item.dItemDeliveryTime).toInt()> millisToMonth(System.currentTimeMillis()).toInt() || (millisToYear(item.dItemDeliveryTime)> millisToYear(itemBefore.dItemDeliveryTime)))
        ) {

            Text(
                text = millisToYear(item.dItemDeliveryTime).toString()+" ${months[millisToMonth(item.dItemDeliveryTime).toInt()-1]} ",
                style = MaterialTheme.typography.h5
            )
        }
        else if((millisToMonth(item.dItemDeliveryTime).toInt()> millisToMonth(itemBefore.dItemDeliveryTime).toInt())
                && !item.dItemCompleted && itemBefore.dItemDeliveryTime==0L) {

                Text(
                    text = millisToYear(item.dItemDeliveryTime).toString()+" ${months[millisToMonth(item.dItemDeliveryTime).toInt()-1]} ",
                    style = MaterialTheme.typography.h5
                )


        }
        //Week
        if(((millisToWeekNumber(item.dItemDeliveryTime).toInt()> millisToWeekNumber(itemBefore.dItemDeliveryTime).toInt()) || (millisToYear(item.dItemDeliveryTime)> millisToYear(itemBefore.dItemDeliveryTime)) )
            && !item.dItemCompleted
            && (millisToWeekNumber(item.dItemDeliveryTime).toInt()> millisToWeekNumber(System.currentTimeMillis()).toInt() || ((millisToYear(item.dItemDeliveryTime)> millisToYear(itemBefore.dItemDeliveryTime))&& itemBefore.dItemDeliveryTime!=0L))
        ) {

            Text(
                text = "WEEK ${millisToWeekNumber(item.dItemDeliveryTime)}",
                style = MaterialTheme.typography.h5
            )
        }
        else {
            if((millisToWeekNumber(item.dItemDeliveryTime).toInt()> millisToWeekNumber(itemBefore.dItemDeliveryTime).toInt())
                && !item.dItemCompleted && itemBefore.dItemDeliveryTime==0L) {

                Text(
                    text = "WEEK ${millisToWeekNumber(System.currentTimeMillis())}",
                    style = MaterialTheme.typography.h5
                )
            }

        }
    }
}