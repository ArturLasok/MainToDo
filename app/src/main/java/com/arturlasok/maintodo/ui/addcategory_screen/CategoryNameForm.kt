package com.arturlasok.maintodo.ui.addcategory_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CategoryNameForm(
    nameValue:String,
    onValueChange:(text:String) -> Unit,
    modifier: Modifier = Modifier,
    onDone:() -> Unit
) {

        TextField(
            enabled = true,
            value = nameValue.replaceFirstChar { it.uppercase() },
            onValueChange = { value-> if(value.length<20) { onValueChange(value) } },
            modifier = modifier.fillMaxWidth().padding(0.dp).height(50.dp),
            singleLine = true,
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