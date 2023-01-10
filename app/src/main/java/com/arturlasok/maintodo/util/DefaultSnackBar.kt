package com.arturlasok.maintodo.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss:() -> Unit
){
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar =  { data ->
            Snackbar(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.padding(start =4.dp,end = 4.dp,top = 4.dp, bottom = 4.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.background
                    )
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary
                            )

                        ) {

                            Text(
                                text = actionLabel,
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.background,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                elevation = 10.dp
            )
        },
        modifier = modifier
    )
}