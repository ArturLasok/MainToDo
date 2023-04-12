package com.arturlasok.maintodo.util


import android.util.Log
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalTime
import java.sql.Time
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

fun millisToDate(milisec: Long) : String {
    val calendar = Calendar.getInstance()
   // val calendar = Calendar.getInstance()
    calendar.timeInMillis = milisec
    var mH = calendar[Calendar.HOUR_OF_DAY]
    if(mH==-1) mH = 23

    val mm = calendar[Calendar.MINUTE]
    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]
   // Log.i(TAG,"Alarm Epoch DAYS : ${mDay}/${mMonth}/${mYear} ${mH}:${mm} ${milisec}")
    //return "${mDay}/${mMonth}/${mYear}"
    return "${mYear}-${mMonth}-${if(mDay<10) "0" else ""}${mDay}"
}
fun millisToMonth(milisec: Long) : String {
    val calendar = Calendar.getInstance()
    //val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    return "${mMonth}"
}
fun millisToYear(milisec: Long) : Int {
    val calendar = Calendar.getInstance()
   // val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]

    return mYear
}
fun millisToDay(milisec: Long) : String {
    val calendar = Calendar.getInstance()
    //val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mDay = calendar[Calendar.DAY_OF_MONTH]

    return "${mDay}"
}
fun millisToWeekNumber(milisec: Long) : String {
    val calendar = Calendar.getInstance()
    //val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mWeek = calendar[Calendar.WEEK_OF_YEAR]

    return "${mWeek}"
}
fun millisToHourOfDay(milisec: Long) :String {

    val hours = TimeUnit.MILLISECONDS.toHours(milisec)

    val minutes = TimeUnit.MILLISECONDS.toMinutes(milisec)-(TimeUnit.MILLISECONDS.toHours(milisec)*60)

     return if(hours<10) {"0"} else {""} + hours.toString() +":"+if (minutes<10) {"0"} else {""}+(minutes).toString()

}

fun millisToHour(milisec: Long) : String {
    val calendar = Calendar.getInstance()
    //val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    var mH = calendar[Calendar.HOUR_OF_DAY]
    if(mH==-1) mH = 23

    val mm = calendar[Calendar.MINUTE]

   return "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"

}

fun millisToDateAndHour(milisec: Long) : String {
    val calendar = Calendar.getInstance()
    //val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]

    val ss = calendar[Calendar.SECOND]

    var mH = calendar[Calendar.HOUR_OF_DAY]
    if(mH==-1) mH = 23

    val mm = calendar[Calendar.MINUTE]

    return "${mYear}-${mMonth}-${if(mDay<10) "0" else ""}${mDay}T${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm:${if(ss<10) "0" else ""}$ss"
    //return "${mDay}/${mMonth}/${mYear} ${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"
}