package com.arturlasok.maintodo.util

import java.util.*

fun millisToDate(milisec: Long) : String {

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]


    return "${mDay}/${mMonth}/${mYear}"
}
