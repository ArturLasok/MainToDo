package com.arturlasok.maintodo.ui.settings_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
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
fun AcceptQuestionSettings(
    isQuestionActive: Boolean,
    changeMode:() -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start= 14.dp, end = 14.dp, top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val checkedState = remember { mutableStateOf(isQuestionActive) }

        Text(
            UiText.StringResource(R.string.settings_change_acc_question,"asd").asString(),
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.SemiBold

            )

        Switch(
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = MaterialTheme.colors.primary,
                checkedThumbColor = MaterialTheme.colors.secondary
            ),
            modifier = Modifier.height(40.dp),
            checked = isQuestionActive,
            onCheckedChange = { changeMode()  }
        )

    }

}