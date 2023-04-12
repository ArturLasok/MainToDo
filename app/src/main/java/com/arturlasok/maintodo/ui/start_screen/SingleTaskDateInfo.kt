package com.arturlasok.maintodo.ui.start_screen

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.util.MainTimeDate
import com.arturlasok.maintodo.util.DateInfo
import com.arturlasok.maintodo.util.TAG
import com.arturlasok.maintodo.util.UiText
import com.arturlasok.maintodo.util.millisToDate
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Composable
fun SingleTaskDateInfo(item: ItemToDo, itemBefore: ItemToDo, itemNext: ItemToDo, firstVisibleIndexX: State<MutableState<Int>>, indexOfItem: Int, isDarkModeOn: Boolean) {

       // val items = startViewModel.tasksFromRoom.collectAsState().value

        //val itemBefore = try { items[indexOfItem-1] } catch (e:Exception) { ItemToDo() }

        //val itemNext = try { items[indexOfItem+1] } catch (e:Exception) { ItemToDo() }



    Log.i(TAG,"System: ${LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay()} >>>>> itemindexed ${indexOfItem }>>>>>> BEFORE: ${itemBefore.dItemTitle}(${LocalDate.parse(millisToDate(itemBefore.dItemDeliveryTime)).toEpochDay()})  /////////  ITEM: ${item.dItemTitle}(${LocalDate.parse(millisToDate(item.dItemDeliveryTime)).toEpochDay()})  //////// NEXT: ${itemNext.dItemTitle}(${LocalDate.parse(millisToDate(itemNext.dItemDeliveryTime)).toEpochDay()})")

/*
   if((item.dItemDeliveryTime!=0L && (TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis()))
               && TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime) != TimeUnit.MILLISECONDS.toDays(itemBefore.dItemDeliveryTime) && firstVisibleIndexX.value.value!=indexOfItem) || (itemNext.dItemTitle.isEmpty() && TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)> TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis())
               || (itemBefore.dItemTitle.isEmpty() && itemNext.dItemTitle.isEmpty()) || (indexOfItem == firstVisibleIndexX.value.value && itemNext.dItemCompleted == true && (TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime)>= TimeUnit.MILLISECONDS.toDays(MainTimeDate.systemCurrentTimeInMillis())))

               )
*/
//Log.i(TAG,"Single date info kolo ITEM BEFOR:${itemBefore.dItemTitle}(epoch: ${LocalDate.parse(millisToDate(itemBefore.dItemDeliveryTime)).toEpochDay()} ) ITEM:${item.dItemTitle} ")
 if((item.dItemDeliveryTime!=0L && (LocalDate.parse(millisToDate(item.dItemDeliveryTime)).toEpochDay()> LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay())
               && LocalDate.parse(millisToDate(item.dItemDeliveryTime)).toEpochDay() != LocalDate.parse(millisToDate(itemBefore.dItemDeliveryTime)).toEpochDay() && firstVisibleIndexX.value.value!=indexOfItem) || (itemNext.dItemTitle.isEmpty() && LocalDate.parse(millisToDate(item.dItemDeliveryTime)).toEpochDay()> LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay()
               || (itemBefore.dItemTitle.isEmpty() && itemNext.dItemTitle.isEmpty()) || (indexOfItem == firstVisibleIndexX.value.value && itemNext.dItemCompleted == true && (LocalDate.parse(millisToDate(item.dItemDeliveryTime)).toEpochDay()>= LocalDate.parse(millisToDate(MainTimeDate.systemCurrentTimeInMillis())).toEpochDay()))

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
     if(((LocalDate.parse(millisToDate(item.dItemDeliveryTime)).toEpochDay() !=LocalDate.parse(millisToDate(itemNext.dItemDeliveryTime)).toEpochDay()) )
         && (indexOfItem == firstVisibleIndexX.value.value && LocalDate.parse(millisToDate(itemNext.dItemDeliveryTime)).toEpochDay()>LocalDate.parse(
             millisToDate(System.currentTimeMillis())).toEpochDay())
        /*
        if(((TimeUnit.MILLISECONDS.toDays(item.dItemDeliveryTime) != TimeUnit.MILLISECONDS.toDays(itemNext.dItemDeliveryTime)) )
            && (indexOfItem == firstVisibleIndexX.value.value && TimeUnit.MILLISECONDS.toDays(itemNext.dItemDeliveryTime)>TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()) )

         */
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