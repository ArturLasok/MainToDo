package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.util.CategoryIconList

@Composable
fun CategoryColumn(
    isDarkModeOn: Boolean,
    categoryRowState: LazyListState,
    categoryList: List<CategoryToDo>,
    selectedCategory: Long,
    onClick:(itemId: Long)-> Unit,
    startScreenUiState: StartScreenState,
    navigateTo:(route: String) -> Unit,
    numberOfItems: SnapshotStateList<Pair<String, Int>>,
    countItems:(categoryToken: String) -> Unit

) {
    //Category row
    LazyColumn(
        state = categoryRowState,
        modifier = Modifier.padding(bottom = 0.dp, end = 10.dp)
    ) {

        itemsIndexed(items = categoryList) { index, item ->
            //Add category button to end of row. Navigation to AddCategory Screen.
            if (index==0) {
                StartCategoryButton(
                    modifier = Modifier.padding(top = 6.dp, start = 12.dp),
                    sizeImage = 32,
                    sizeCircle = 64,
                    image = if (isDarkModeOn || (selectedCategory != -1L)) R.drawable.addcat_dark
                    else R.drawable.addcat_light,
                    imageDesc = "Add icon image",
                    text = "All tasks",
                    imageModifier = Modifier,
                    isDarkModeOn = isDarkModeOn,
                    clicked = { onClick(-1L) },
                    selected = selectedCategory == -1L,
                    ifSelected = {},
                    startScreenState = startScreenUiState,
                    numberOfItems = 0,
                    countItems = {  }
                )
            }
            //Category from db
            StartCategoryButton(
                modifier = Modifier.padding(top = 6.dp, start = 12.dp),
                sizeImage = 32,
                sizeCircle = 64,
                image = if (isDarkModeOn || (selectedCategory != item.dCatId)) CategoryIconList.getIconsDark()[item.dCatIcon
                    ?: 0] else CategoryIconList.getIconsLight()[item.dCatIcon ?: 0],
                imageDesc = "Add icon image",
                text = item.dCatName ?: "",
                imageModifier = Modifier,
                isDarkModeOn = isDarkModeOn,
                clicked = { onClick(item.dCatId ?: -1L) },
                selected = selectedCategory == item.dCatId,
                ifSelected = { },
                startScreenState = StartScreenState.Welcome,
                numberOfItems =  numberOfItems.find {
                    it.first == item.dCatToken
                }?.second ?: 0,
                countItems = { countItems(item.dCatToken ?: "") }
            )
            //Add category button to end of row. Navigation to AddCategory Screen.
            if (categoryList.size == index + 1) {
                StartCategoryButton(
                    modifier = Modifier.padding(top = 6.dp, start = 12.dp),
                    sizeImage = 32,
                    sizeCircle = 64,
                    image = if (isDarkModeOn) R.drawable.addcat_dark else R.drawable.addcat_light,
                    imageDesc = "Add icon image",
                    text = "Add new category",
                    imageModifier = Modifier,
                    isDarkModeOn = isDarkModeOn,
                    clicked = { navigateTo(Screen.AddCategory.route) },
                    selected = true,
                    ifSelected = {},
                    startScreenState = startScreenUiState,
                    numberOfItems = 0,
                    countItems = {  }

                )
            }


        }


    }
    //Add category button if no items yet. Navigation to AddCategory Screen.
    if (categoryList.isEmpty()) {
        Column() {


            StartCategoryButton(
                modifier = Modifier.padding(top = 6.dp, start = 12.dp),
                sizeImage = 32,
                sizeCircle = 64,
                image = if (isDarkModeOn || (selectedCategory != -1L)) R.drawable.addcat_dark
                else R.drawable.addcat_light,
                imageDesc = "Add icon image",
                text = "All tasks",
                imageModifier = Modifier,
                isDarkModeOn = isDarkModeOn,
                clicked = { onClick(-1L) },
                selected = selectedCategory == -1L,
                ifSelected = {},
                startScreenState = startScreenUiState,
                numberOfItems = 0,
                countItems = {  }
            )
            StartCategoryButton(
                modifier = Modifier.padding(top = 6.dp, start = 12.dp, end = 10.dp),
                sizeImage = 32,
                sizeCircle = 64,
                image = if (isDarkModeOn) R.drawable.addcat_dark else R.drawable.addcat_light,
                imageDesc = "Add icon image",
                text = "Add new category",
                imageModifier = Modifier,
                isDarkModeOn = isDarkModeOn,
                clicked = { navigateTo(Screen.AddCategory.route) },
                selected = false,
                ifSelected = {},
                startScreenState = startScreenUiState,
                numberOfItems = 0,
                countItems = {  }
            )
        }
    }
}