package com.arturlasok.maintodo.ui.addcategory_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.util.CategoryIconList


@Composable
fun CategorySelector(
    isDarkModeOn: Boolean,
    selected: Int,
    selectedNewVal:(newVal:Int) ->Unit

) {

    val categoryItems = if(isDarkModeOn) CategoryIconList.getIconsDark() else CategoryIconList.getIconsLight()
    val rowState = rememberLazyListState()

    LazyRow(state = rowState) {

        itemsIndexed(items = categoryItems) { index, item ->

            CategoryButton(
                modifier = Modifier.padding(top = 20.dp, start = 12.dp),
                sizeImage = 34,
                sizeCircle = 64,
                image = item,
                imageModifier = Modifier,
                isDarkModeOn = isDarkModeOn,
                clicked = { selectedNewVal(index) },
                selected= selected==index
            )

        }
    }
    LaunchedEffect(key1 = selected, block = {
        if(selected>-1) {
            rowState.animateScrollToItem(selected)
        }

    })
}