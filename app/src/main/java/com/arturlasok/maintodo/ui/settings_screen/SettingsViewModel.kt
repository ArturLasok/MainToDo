package com.arturlasok.maintodo.ui.settings_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: BaseApplication,
) : ViewModel() {


    fun dateWithNameOfDayWeek() : String {
        val timeInMilis =  System.currentTimeMillis()
        val dayOfWeek = milisToDayOfWeek(timeInMilis)

        val dayNames = listOf<String>(
            UiText.StringResource(R.string.day1,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day2,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day3,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day4,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day5,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day6,"asd").asString(application.applicationContext),
            UiText.StringResource(R.string.day7,"asd").asString(application.applicationContext)
        )

        return millisToDate(timeInMilis)+ " " + dayNames[dayOfWeek-2]

    }

}