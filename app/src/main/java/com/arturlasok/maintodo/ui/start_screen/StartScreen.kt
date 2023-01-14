package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.util.CategoryIconList
import com.arturlasok.maintodo.util.UiText

@Composable
fun StartScreen(
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Boolean,
    startViewModel:  StartViewModel  = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val items = startViewModel.categoriesFromRoom.collectAsState(initial = listOf()).value
    val selectedCategory = startViewModel.selectedCategory.collectAsState(initial = -1L).value

    Scaffold(
        scaffoldState =  scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        topBar = {},
        bottomBar = {},
        backgroundColor = Color.Transparent

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                //Hi Text
                Column {

                    Text(
                        text = UiText.StringResource(
                            R.string.app_welcome,
                            "asd"
                        ).asString(),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primary
                    )
                    Text(
                        text= startViewModel.dateWithNameOfDayWeek(),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary

                    )
                }
                //Settings Button
                Column {

                    SettingsButton(
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier,
                        onClick = {navigateTo(Screen.Settings.route)}
                    )

                }

            }
            //Category row
            LazyRow(
                state = rememberLazyListState(),
                modifier = Modifier.padding(bottom = 10.dp)
            ) {

                itemsIndexed(items = items) { index, item ->
                    //Category from db
                    StartCategoryButton(
                        modifier = Modifier.padding(top = 24.dp, start = 12.dp),
                        sizeImage = 42,
                        sizeCircle = 64,
                        image = if(isDarkModeOn) CategoryIconList.getIconsDark()[item.dCatIcon ?: 0] else CategoryIconList.getIconsLight()[item.dCatIcon ?: 0],
                        imageDesc = "Add icon image",
                        text = item.dCatName ?: "",
                        imageModifier = Modifier,
                        isDarkModeOn = isDarkModeOn,
                        clicked = { startViewModel.setSelectedCategory(item.dCatId ?: -1) },
                        selected = selectedCategory==item.dCatId
                    )
                    //Add category button to end of row. Navigation to AddCategory Screen.
                    if(items.size==index+1) {
                        StartCategoryButton(
                            modifier = Modifier.padding(top = 24.dp, start = 12.dp),
                            sizeImage = 42,
                            sizeCircle = 64,
                            image = if(isDarkModeOn) R.drawable.addcat_dark else R.drawable.addcat_light,
                            imageDesc = "Add icon image",
                            text = "Add new category",
                            imageModifier = Modifier,
                            isDarkModeOn = isDarkModeOn,
                            clicked = { navigateTo(Screen.AddCategory.route)},
                            selected = false
                        )
                    }


                }


            }
            //Category details for selected category
            if(startViewModel.getOneFromCategoryList(selectedCategory).dCatId != null) {
                CategoryDetails(
                    selectedCategoryDetails = startViewModel.getOneFromCategoryList(selectedCategory),
                    navigateTo = { route -> navigateTo(route) },
                    isDarkModeOn = isDarkModeOn,
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                )
            }



        }


    }



}