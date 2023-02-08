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
fun millisToHour(milisec: Long) : String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]


    var mH = calendar[Calendar.HOUR_OF_DAY]-1
    if(mH==-1) mH = 23

    val mm = calendar[Calendar.MINUTE]


    return "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"

}


fun millisToDateAndHour(milisec: Long) : String {

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]


    var mH = calendar[Calendar.HOUR_OF_DAY]-1
    if(mH==-1) mH = 23

    val mm = calendar[Calendar.MINUTE]


    return "${mDay}/${mMonth}/${mYear} ${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"
}