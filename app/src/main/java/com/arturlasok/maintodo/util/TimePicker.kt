package com.arturlasok.maintodo.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TimePicker(
    isDarkModeOn: Boolean,
    initialTime: LocalTime,
    light_img: Int,
    dark_img: Int,
    modifier: Modifier,
    setTime:(time: Long) -> Unit,
    acceptedDate: Long,
    dialogState: MaterialDialogState
    ) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton(UiText.StringResource(R.string.cancel,"asd").asString())
        }
    ) {


        timepicker(
            title = UiText.StringResource(R.string.select_time,"asd").asString(),
            initialTime = initialTime,
            timeRange =
            if(acceptedDate == LocalDate.now().toEpochDay()) { LocalTime.now()..LocalTime.MAX } else { LocalTime.MIN..LocalTime.MAX},
            is24HourClock = true
        ) { time ->
            // Do stuff with java.time.LocalDate object which is passed in
            setTime(time.toKotlinLocalTime().toMillisecondOfDay().toLong())
        }
    }





    Image(
        bitmap = ImageBitmap.imageResource(
            id = if(!isDarkModeOn) { dark_img } else { light_img }
        ),
        modifier = modifier
            .size(
                32.dp,
                32.dp
            ),
        contentDescription = "Pick Date",
        colorFilter = if(isDarkModeOn) { null  } else {
            null }
    )
}