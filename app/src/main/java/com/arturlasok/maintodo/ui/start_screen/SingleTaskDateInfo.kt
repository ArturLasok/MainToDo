package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.util.DateInfo
import com.arturlasok.maintodo.util.UiText
import java.util.concurrent.TimeUnit

@Composable
fun SingleTaskDateInfo(item: ItemToDo, itemBefore: ItemToDo, itemNext: ItemToDo, firstVisibleIndexX: State<MutableState<Int>>, indexOfItem: Int, isDarkModeOn: Boolean) {
    if((item.dItemDeliveryTime!=0L && (TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()))
                && TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime) != TimeUnit.MILLISECONDS.toDays(itemBefore.dItemDeliveryTime) && firstVisibleIndexX.value.value!=indexOfItem) || (itemNext.dItemTitle.isEmpty() && TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())
                || (itemBefore.dItemTitle.isEmpty() && itemNext.dItemTitle.isEmpty()) || (indexOfItem == firstVisibleIndexX.value.value && itemNext.dItemCompleted == true && (TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)>= TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())))

                )
    ) {
        DateInfo(
            itemBefore = itemBefore,
            sizeCircle = 38.dp,
            isDarkModeOn = isDarkModeOn,
            completed = item.dItemCompleted,
            date = item.dItemDeliveryTime,
            days = listOf<String>(
                UiText.StringResource(R.string.day7, "asd").asString(),
                UiText.StringResource(R.string.day1, "asd").asString(),
                UiText.StringResource(R.string.day2, "asd").asString(),
                UiText.StringResource(R.string.day3, "asd").asString(),
                UiText.StringResource(R.string.day4, "asd").asString(),
                UiText.StringResource(R.string.day5, "asd").asString(),
                UiText.StringResource(R.string.day6, "asd").asString(),

                )
        )

    } else {
        if(((TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime) != TimeUnit.MILLISECONDS.toDays(itemNext.dItemDeliveryTime)) )
            && (indexOfItem == firstVisibleIndexX.value.value && TimeUnit.MILLISECONDS.toDays(itemNext.dItemDeliveryTime)>TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) )
        )
        {
            DateInfo(
                itemBefore = itemBefore,
                sizeCircle = 38.dp,
                isDarkModeOn = isDarkModeOn,
                completed = item.dItemCompleted,
                date = if(TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())) { item.dItemDeliveryTime} else { System.currentTimeMillis() },
                days = listOf<String>(
                    UiText.StringResource(R.string.day7, "asd").asString(),
                    UiText.StringResource(R.string.day1, "asd").asString(),
                    UiText.StringResource(R.string.day2, "asd").asString(),
                    UiText.StringResource(R.string.day3, "asd").asString(),
                    UiText.StringResource(R.string.day4, "asd").asString(),
                    UiText.StringResource(R.string.day5, "asd").asString(),
                    UiText.StringResource(R.string.day6, "asd").asString(),

                    )
            )
        }
    }

}