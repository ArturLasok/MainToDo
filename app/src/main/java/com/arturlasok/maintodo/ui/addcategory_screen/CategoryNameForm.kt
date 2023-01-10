package com.arturlasok.maintodo.ui.addcategory_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CategoryNameForm(
    nameValue:String,
    onValueChange:(text:String) -> Unit,
    modifier: Modifier = Modifier,
    onDone:() -> Unit
) {

    BasicTextField(
        value = nameValue,
        onValueChange = { value-> if(value.length<30) { onValueChange(value) } },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.primary),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {

            onDone()

        }),
    )
}