package com.arturlasok.maintodo.util


import java.util.*
import java.util.concurrent.TimeUnit

fun millisToDate(milisec: Long) : String {

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]

    return "${mDay}/${mMonth}/${mYear}"
}
fun millisToEpochDays(milisec: Long) : String {

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_YEAR]


    return "${mDay}"
}
fun millisToMonth(milisec: Long) : String {

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]


    return "${mMonth}"
}
fun millisToYear(milisec: Long) : Int {

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]



    return mYear
}
fun millisToDay(milisec: Long) : String {

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]


    return "${mDay}"
}
fun millisToWeekNumber(milisec: Long) : String {

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]
    val mWeek = calendar[Calendar.WEEK_OF_YEAR]

    return "${mWeek}"
}
fun millisToHourOfDay(milisec: Long) :String {

    val hours = TimeUnit.MILLISECONDS.toHours(milisec)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(milisec)-(TimeUnit.MILLISECONDS.toHours(milisec)*60)



    return if(hours<10) {"0"} else {""} + hours.toString() +":"+if (minutes<10) {"0"} else {""}+(minutes).toString()
   // return if(hours<10) {"0"} else {""} + hours.toString() +":"+if (minutes<10) {"0"} else {""}+(minutes).toString()
}

fun millisToHour(milisec: Long) : String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]

    val mPm = calendar[Calendar.AM_PM]
    val mHPm = calendar[Calendar.HOUR]

    var mH = calendar[Calendar.HOUR_OF_DAY]
    if(mH==-1) mH = 23

    val mm = calendar[Calendar.MINUTE]

    //return "$mHPm:$mm, $mPm"
   return "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"

}


fun millisToDateAndHour(milisec: Long) : String {

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]


    var mH = calendar[Calendar.HOUR_OF_DAY]
    if(mH==-1) mH = 23

    val mm = calendar[Calendar.MINUTE]


    return "${mDay}/${mMonth}/${mYear} ${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"
}