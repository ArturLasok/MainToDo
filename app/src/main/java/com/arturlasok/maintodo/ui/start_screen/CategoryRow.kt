package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.util.CategoryIconList

@Composable
fun CategoryRow(
    isDarkModeOn: Boolean,
    categoryRowState: LazyListState,
    categoryList: List<CategoryToDo>,
    selectedCategory: Long,
    onClick:(itemId: Long)-> Unit,
    startScreenUiState: StartScreenState,
    navigateTo:(route: String) -> Unit

) {
    //Category row
    LazyRow(
        state = categoryRowState,
        modifier = Modifier.padding(bottom = 10.dp)
    ) {

        itemsIndexed(items = categoryList) { index, item ->
            //Add category button to end of row. Navigation to AddCategory Screen.
            if (index==0) {
                StartCategoryButton(
                    modifier = Modifier.padding(top = 24.dp, start = 12.dp),
                    sizeImage = 34,
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
                    startScreenState = startScreenUiState
                )
            }
            //Category from db
            StartCategoryButton(
                modifier = Modifier.padding(top = 24.dp, start = 12.dp),
                sizeImage = 34,
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
                startScreenState = StartScreenState.Welcome
            )
            //Add category button to end of row. Navigation to AddCategory Screen.
            if (categoryList.size == index + 1) {
                StartCategoryButton(
                    modifier = Modifier.padding(top = 24.dp, start = 12.dp),
                    sizeImage = 34,
                    sizeCircle = 64,
                    image = if (isDarkModeOn) R.drawable.addcat_dark else R.drawable.addcat_light,
                    imageDesc = "Add icon image",
                    text = "Add new category",
                    imageModifier = Modifier,
                    isDarkModeOn = isDarkModeOn,
                    clicked = { navigateTo(Screen.AddCategory.route) },
                    selected = true,
                    ifSelected = {},
                    startScreenState = startScreenUiState

                )
            }


        }


    }
    //Add category button if no items yet. Navigation to AddCategory Screen.
    if (categoryList.isEmpty()) {
        StartCategoryButton(
            modifier = Modifier.padding(top = 24.dp, start = 12.dp),
            sizeImage = 34,
            sizeCircle = 64,
            image = if (isDarkModeOn) R.drawable.addcat_dark else R.drawable.addcat_light,
            imageDesc = "Add icon image",
            text = "Add new category",
            imageModifier = Modifier,
            isDarkModeOn = isDarkModeOn,
            clicked = { navigateTo(Screen.AddCategory.route) },
            selected = false,
            ifSelected = {},
            startScreenState = startScreenUiState
        )
    }
}