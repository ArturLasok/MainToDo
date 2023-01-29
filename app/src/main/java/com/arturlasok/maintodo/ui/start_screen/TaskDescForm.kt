package com.arturlasok.maintodo.ui.start_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.util.UiText

@Composable
fun TaskDescForm(
    nameValue:String,
    onValueChange:(text:String) -> Unit,
    modifier: Modifier = Modifier,
    isDarkModeOn: Boolean,
    onDone:() -> Unit
) {
    //
    //add desc surface
    //

    Text(
        text = UiText.StringResource(
            com.arturlasok.maintodo.R.string.addtask_form_taskdesc,
            "no"
        ).asString(),
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(start = 10.dp, bottom = 4.dp),
        color = MaterialTheme.colors.primary
    )
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .height(100.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = if (isDarkModeOn) {
            MaterialTheme.colors.onSecondary
        } else {
            MaterialTheme.colors.surface
        },
        elevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()

        ) {
        TextField(
            enabled = true,
            value = nameValue.replaceFirstChar { it.uppercase() },
            onValueChange = { value-> if(value.length<360) { onValueChange(value) } },
            modifier = modifier.fillMaxWidth().padding(0.dp).height(100.dp),
            singleLine = false,
            textStyle = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.primary),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {

                onDone()

            }),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = MaterialTheme.colors.primary
            ),
            trailingIcon = {
                if(nameValue.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            Icons.Filled.Clear, "Clear",
                            modifier = Modifier
                                .padding(start = 14.dp, end = 14.dp),
                            tint = Color.Gray
                        )

                    }
                }
            }

        )
        }
    }
}