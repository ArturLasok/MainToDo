package com.arturlasok.maintodo.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R

@Composable
fun RemoveAlert(
    question: String,
    onYes:() -> Unit,
    onCancel:() -> Unit,
    onDismiss:() ->Unit
){
    AlertDialog(
        //modifier = Modifier.border(1.dp, MaterialTheme.colors.primaryVariant),
        onDismissRequest = {
            onDismiss()
        },
        shape = MaterialTheme.shapes.medium,
        text = {
               Text(
                   text = question,
                   style = MaterialTheme.typography.h2,
                   textAlign = TextAlign.Center
               )
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    modifier = Modifier.padding(2.dp),
                    onClick = { onCancel() }
                ) {
                    Text(UiText.StringResource(R.string.edit_alert_cancel, "asd").asString())
                }
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    modifier = Modifier.padding(2.dp),
                    onClick = { onYes() }
                ) {
                    Text(
                        text= UiText.StringResource(R.string.edit_alert_yes, "asd").asString(),
                        color = Color.White

                    )
                }


            }
        }
    )

}