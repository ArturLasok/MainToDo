package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.util.UiText

@Composable
fun AddTaskButton(
    hideKeyboard:() ->Unit,
    verifyForm:() -> Unit
) {
    //
    //Add task button
    //
    val addButtonEnabled = rememberSaveable { mutableStateOf(true) }
    Button(
        enabled = addButtonEnabled.value,
        onClick = {
            //addButtonEnabled.value = false
            hideKeyboard()
            verifyForm()
        },
        modifier = Modifier.padding(0.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = UiText.StringResource(
                R.string.add,
                "no"
            ).asString().uppercase(),
            style = MaterialTheme.typography.h4,

        )
    }
}