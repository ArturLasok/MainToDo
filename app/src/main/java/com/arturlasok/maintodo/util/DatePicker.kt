package com.arturlasok.maintodo.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.arturlasok.maintodo.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

@Composable
fun DatePicker(
    taskDataType:Boolean,
    isDarkModeOn: Boolean,
    initialDate: LocalDate,
    taskLimitDate: LocalDate,
    light_img: Int,
    dark_img: Int,
    modifier: Modifier,
    setDate:(date: Long) -> Unit,
    dialogState: MaterialDialogState
    ) {


    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton("Ok")
            negativeButton(UiText.StringResource(R.string.cancel,"asd").asString())
        }
    ) {

        datepicker(
            title = UiText.StringResource(R.string.select_date,"asd").asString(),
            initialDate = initialDate,
            allowedDateValidator = {
                   if(taskDataType || taskLimitDate.toEpochDay()==0L) {
                       ((it.dayOfMonth>= LocalDate.now().dayOfMonth && it.month>=LocalDate.now().month) || (
                               it.month>LocalDate.now().month
                               ) || it.year>LocalDate.now().year)

                   }
                else {
                    it.toEpochDay()>=LocalDate.now().toEpochDay() && it.toEpochDay()<=taskLimitDate.toEpochDay()
                    /*
                ((it.dayOfMonth>= LocalDate.now().dayOfMonth && it.month>=LocalDate.now().month) || (
                    it.month>LocalDate.now().month
                    )) && ((it.dayOfMonth<=taskLimitDate.dayOfMonth && it.month<=taskLimitDate.month) || (
                        it.month<taskLimitDate.month
                        ) || (it.year<=taskLimitDate.year && it.year>LocalDate.now().year))


                     */
                   }
        }) { date ->
            setDate(date.toEpochDay())
            // Do stuff with java.time.LocalDate object which is passed in
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
        colorFilter = if(isDarkModeOn) { null } else {
            null }
    )
}