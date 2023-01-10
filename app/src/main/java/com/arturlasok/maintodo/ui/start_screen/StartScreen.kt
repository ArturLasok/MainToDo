package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.util.UiText

@Composable
fun StartScreen(
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Boolean,
    startViewModel:  StartViewModel  = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()


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

                    SettingButton(
                        isDarkModeOn = isDarkModeOn,
                        modifier = Modifier,
                        onClick = {navigateTo(Screen.Settings.route)}
                    )

                }

            }

        CategoryAddButton(
            modifier = Modifier.padding(top = 24.dp, start = 12.dp),
            sizeImage = 42,
            sizeCircle = 64,
            image = if(isDarkModeOn) R.drawable.addcat_dark else R.drawable.addcat_light,
            imageModifier = Modifier,
            isDarkModeOn = isDarkModeOn,
            clicked = { navigateTo(Screen.AddCategory.route)}
        )

        }


    }



}