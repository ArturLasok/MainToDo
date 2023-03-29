package com.arturlasok.maintodo.ui.start_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.util.*

@Composable
fun ListInfos(
    isDarkModeOn: Boolean,
    months: List<String>,
    firstVisibleIndexX: State<MutableState<Int>>,
    itemBefore: ItemToDo,
    itemNext: ItemToDo,
    item: ItemToDo,
    modifier: Modifier,

    ) {
    //Log.i(TAG,"item year ${(millisToYear(item.dItemDeliveryTime))} itembefor year${(millisToYear(itemBefore.dItemDeliveryTime))} ")
    Log.i(TAG,"ITEM recompose ${item.dItemTitle}")



    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {

        Surface(  modifier = Modifier
            //.fillMaxWidth()
            .background(
                if (!isDarkModeOn) {
                    Color.White
                } else {
                    MaterialTheme.colors.onSecondary
                }, MaterialTheme.shapes.medium
            )
            //.padding(end = 8.dp)
            .width(130.dp),
        shape = MaterialTheme.shapes.medium,
            elevation = 10.dp
            )


        {
    Row(modifier = Modifier.padding(end = 8.dp),
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
                text = UiText.StringResource(
                    R.string.week_number,
                    "asd"
                ).asString()+" ${millisToWeekNumber(item.dItemDeliveryTime)}",
                style = MaterialTheme.typography.h5
            )
        }
        else {
            if((millisToWeekNumber(item.dItemDeliveryTime).toInt()> millisToWeekNumber(itemBefore.dItemDeliveryTime).toInt())
                && !item.dItemCompleted && itemBefore.dItemDeliveryTime==0L) {

                Text(
                    text = UiText.StringResource(
                        R.string.week_number,
                        "asd"
                    ).asString()+" ${millisToWeekNumber(System.currentTimeMillis())}",
                    style = MaterialTheme.typography.h5
                )
            }

        }
    }
    }
    }
}