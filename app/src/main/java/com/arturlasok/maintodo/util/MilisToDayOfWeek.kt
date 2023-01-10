package com.arturlasok.maintodo.util

import java.util.*

fun milisToDayOfWeek(milisec: Long) : Int {

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milisec

    val dayOfweek = calendar[Calendar.DAY_OF_WEEK]

    return dayOfweek
}
