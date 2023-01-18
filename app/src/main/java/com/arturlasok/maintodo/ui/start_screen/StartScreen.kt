package com.arturlasok.maintodo.ui.start_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
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
    startViewModel:  StartViewModel  = hiltViewModel(),
    selectedFromNav: Long,
) {


    val scaffoldState = rememberScaffoldState()
    val categoryRowState = rememberLazyListState()
    val categoryList = startViewModel.categoriesFromRoom.collectAsState(initial = listOf()).value
    val selectedCategory = startViewModel.selectedCategory.collectAsState(initial = -1L).value
    val startScreenUiState: StartScreenState = startViewModel.startScreenUiState.value


    //Text("selected index: ${startViewModel.selectedCategoryRowIndex.value}")
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
            //add task top bar
            AddTaskTopBar(
                startScreenUiState = startScreenUiState,
                isDarkModeOn = isDarkModeOn,
                navigateTo = { route ->  navigateTo(route)},
                categoryId = selectedCategory,
                close = { startViewModel.setStartScreenUiState(StartScreenState.Welcome) }
            )

            //title and settings
            AnimatedVisibility(
                visible = startScreenUiState == StartScreenState.Welcome,
                enter = slideInVertically(
                    initialOffsetY = {
                        it - 3*it
                    },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                        delayMillis = 0
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = {
                        it - 3 * it
                    },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing
                    )
                )

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
                            text = startViewModel.dateWithNameOfDayWeek(),
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.primary

                        )
                    }
                    //Settings Button
                    Column {

                        SettingsButton(
                            isDarkModeOn = isDarkModeOn,
                            modifier = Modifier,
                            onClick = { navigateTo(Screen.Settings.route + "/${selectedCategory}") },
                            light_img = R.drawable.settings,
                            dark_img = R.drawable.settings_dark
                        )


                    }

                }
            }
            //Category row
            LazyRow(
                state = categoryRowState,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {

                itemsIndexed(items = categoryList) { index, item ->

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
                        clicked = { startViewModel.setSelectedCategory(item.dCatId ?: -1) },
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
                //Animate category details
                AnimatedVisibility(
                    visible = startViewModel.getOneFromCategoryList(selectedCategory).dCatId != null
                            && startScreenUiState != StartScreenState.AddTask
                    ,
                    enter = slideInHorizontally(
                        initialOffsetX = {
                            it
                        },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing,
                            delayMillis = 0
                        )
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = {
                            it - 3 * it
                        },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )

                ) {
                    CategoryDetails(
                        selectedCategoryDetails = startViewModel.getOneFromCategoryList(
                            selectedCategory
                        ),
                        navigateTo = { route -> navigateTo(route) },
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                    )
                }
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