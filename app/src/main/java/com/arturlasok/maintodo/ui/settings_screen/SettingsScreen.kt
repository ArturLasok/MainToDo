package com.arturlasok.maintodo.ui.settings_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.navigation.Screen
import com.arturlasok.maintodo.util.UiText

@Composable
fun SettingsScreen(
    navigateTo: (route: String) -> Unit,
    isDarkModeOn: Int,
    changeDarkMode:(newVal: Int) -> Unit,
    runLink:(runlink :String) -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {


    Column {

        Box() {

            //Back button
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                BackButton(
                    isDarkModeOn = isDarkModeOn==2 || isSystemInDarkTheme(),
                    modifier = Modifier,
                    onClick = {navigateTo(Screen.Start.route)}
                )
            }
            //Screen title
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {

                Text(text= UiText.StringResource(
                    R.string.app_settings,
                    "no"
                ).asString(),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.primary)
            }

        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
        )

        //Settings surface
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            shape = MaterialTheme.shapes.large,
            color = if(isDarkModeOn==2 || isSystemInDarkTheme()) { MaterialTheme.colors.onSecondary} else { MaterialTheme.colors.surface},
            elevation = 10.dp,
        ) {
            Column(modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState())) {


                Text(
                    UiText.StringResource(R.string.settings_adjust, "asd").asString(),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 4.dp),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colors.primary))
                //Settings adjustment
                DarkModeSettings(
                    isDarkActive = isDarkModeOn,
                    changeMode = { newVal -> changeDarkMode(newVal) }
                )
                //Settings adjustment
                AcceptQuestionSettings(
                    isQuestionActive = false,
                    changeMode = {  }
                )
                Text(
                    UiText.StringResource(R.string.settings_additional_info, "asd").asString(),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 4.dp),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colors.primary))
                //Additional information
                Column(modifier = Modifier.padding(12.dp)) {



                    Text(
                        "Icons from flaticon https://www.flaticon.com",
                        style = MaterialTheme.typography.h4,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable(onClick = {
                            runLink("https://www.flaticon.com")
                        }),
                        color = MaterialTheme.colors.onSurface
                    )
                    Text("Icons created by Freepik - https://www.freepik.com",
                        style = MaterialTheme.typography.h4,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable(onClick = {
                            runLink("https://www.freepik.com")
                        }),
                        color = MaterialTheme.colors.onSurface
                    )
                    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painterResource(R.drawable.flaticon),
                            modifier = Modifier
                                .size(64.dp, 64.dp)
                                .clickable(onClick = {
                                    runLink("https://www.flaticon.com")
                                }),
                            contentDescription = "flaticon.com",
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                        )
                        Image(
                            painterResource(R.drawable.freepik),
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .size(64.dp, 64.dp)
                                .clickable(onClick = {
                                    runLink("https://www.freepik.com")
                                }),
                            contentDescription = "freepik.com",
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                        )
                    }
                }

            }
        }

    }


}