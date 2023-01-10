package com.arturlasok.maintodo.ui.settings_screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.util.UiText

@Composable
fun DarkModeSettings(
    isDarkActive: Int,
    changeMode:(newVal: Int) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp, top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val checkedState = remember { mutableStateOf(isDarkActive) }
        Column() {
            Text(
                UiText.StringResource(R.string.settings_changemode, "asd").asString(),
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.SemiBold

            )
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isDarkActive==0,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.secondary,
                        //checkmarkColor = MaterialTheme.colors.primary
                    ),
                    onCheckedChange = {
                       changeMode(0)
                    },
                    modifier = Modifier.padding(0.dp)
                )
                Text(
                    text =UiText.StringResource(R.string.settings_theme_mode0,"asd").asString(),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.primary)
            }
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isDarkActive==1,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.secondary,
                        //checkmarkColor = MaterialTheme.colors.primary
                    ),
                    onCheckedChange = {
                        changeMode(1)
                    },
                    modifier = Modifier.padding(0.dp)
                )
                Text(
                    text =UiText.StringResource(R.string.settings_theme_mode1,"asd").asString(),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.primary)
            }
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isDarkActive==2,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.secondary,
                        //checkmarkColor = MaterialTheme.colors.primary
                    ),
                    onCheckedChange = {
                        changeMode(2)
                    },
                    modifier = Modifier.padding(0.dp)
                )
                Text(
                    text =UiText.StringResource(R.string.settings_theme_mode2,"asd").asString(),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.primary)
            }


        }
    }

}