package com.arturlasok.maintodo.util

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.interactors.util.MainTimeDate
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

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
            is24HourClock = MainTimeDate.dateIs24h()
        ) { time ->
            // Do stuff with java.time.LocalDate object which is passed in
            setTime(TimeUnit.NANOSECONDS.toMillis(time.withSecond(0).toNanoOfDay()))
            Log.i(TAG,"Calendar ${LocalTime.ofNanoOfDay(time.toNanoOfDay()).hour}  / System current: ${millisToHour(MainTimeDate.systemCurrentTimeInMillis())}")
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