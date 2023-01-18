package com.arturlasok.maintodo.ui.start_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.util.UiText

@Composable
fun StartScreen(
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Boolean,
    startViewModel:  StartViewModel  = hiltViewModel(),
    selectedFromNav: Long,
) {


    val scaffoldState = rememberScaffoldState()
    val categoryRowState = rememberLazyListState()
    val categoryList = startViewModel.categoriesFromRoom.collectAsState(initial = listOf()).value
    val selectedCategory = startViewModel.selectedCategory.collectAsState(initial = -1L).value
    val startScreenUiState: StartScreenState = startViewModel.startScreenUiState.value


    LaunchedEffect(key1 = true, block = {
        if (selectedFromNav != -1L) {
            startViewModel.setSelectedCategory(selectedFromNav)
        }
    })
    BackHandler(enabled = true) {
        if(startScreenUiState==StartScreenState.AddTask) {
            startViewModel.setStartScreenUiState(StartScreenState.Welcome)
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            //FAB
            if(startScreenUiState == StartScreenState.Welcome) {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .alpha(0.6f)
                        .padding(bottom = 10.dp),
                    icon = {
                        Image(
                            bitmap = ImageBitmap.imageResource(id = R.drawable.addpoint_dark),
                            contentDescription = UiText.StringResource(
                                R.string.floating_button_add,
                                "asd"
                            ).asString(),
                            modifier = Modifier
                                .size(24.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary)
                        )
                    },
                    text = {
                        Text(
                            text = UiText.StringResource(
                                R.string.floating_button_add,
                                "asd"
                            ).asString()
                        )
                    },
                    onClick = {
                        startViewModel.setStartScreenUiState(StartScreenState.AddTask)
                    }
                )
            }
        },
        topBar = {},
        bottomBar = {},
        backgroundColor = Color.Transparent

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            //Add task top bar
            AddTaskTopBar(
                startScreenUiState = startScreenUiState,
                isDarkModeOn = isDarkModeOn,
                navigateTo = { route ->  navigateTo(route)},
                categoryId = selectedCategory,
                close = { startViewModel.setStartScreenUiState(StartScreenState.Welcome) }
            )
            //Top info and settings button
            TopAndSettings(
                startScreenUiState = startScreenUiState,
                dateAndNameOfDay = startViewModel.dateWithNameOfDayWeek(),
                navigateTo = { route ->  navigateTo(route)},
                selectedCategory = selectedCategory,
                isDarkModeOn = isDarkModeOn
            )
            //Category row
            CategoryRow(
                isDarkModeOn = isDarkModeOn,
                categoryRowState = categoryRowState,
                categoryList = categoryList,
                selectedCategory = selectedCategory,
                onClick = { itemId ->  startViewModel.setSelectedCategory(itemId) },
                startScreenUiState = startScreenUiState,
                navigateTo = {route ->  navigateTo(route) }
            )
            //Category details
            CategoryDetails(
                visible = startViewModel.getOneFromCategoryList(selectedCategory).dCatId != null
                        && startScreenUiState != StartScreenState.AddTask,
                selectedCategoryDetails = startViewModel.getOneFromCategoryList(
                    selectedCategory
                ),
                navigateTo = { route -> navigateTo(route) },
                isDarkModeOn = isDarkModeOn,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
            )

            //Category anim to selected category
            if (startViewModel.getOneFromCategoryList(selectedCategory).dCatId != null) {
                LaunchedEffect(key1 = true, block = {
                    startViewModel.selectedCategoryRowIndex.value = categoryList.indexOf(
                        categoryList.find {
                            it.dCatId == selectedCategory
                        }
                    )
                    categoryRowState.animateScrollToItem(startViewModel.selectedCategoryRowIndex.value)
                })
            }

        }

    }

}