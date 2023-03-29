package com.arturlasok.maintodo.interactors.util

import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

fun convertMillisToHourFromGmt(milisec: Long) : String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    calendar.timeInMillis = milisec

    calendar.timeZone.id = "GMT"

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]

    val mPm = calendar[Calendar.AM_PM]
    val mHPm = calendar[Calendar.HOUR]

    var mH = calendar[Calendar.HOUR_OF_DAY]
    //if(mH==-1) mH = 23

    val mm = calendar[Calendar.MINUTE]

    //return "$mHPm:$mm, $mPm"
    return "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"

}
class MainTimeDate {


    fun convertMillisecondsToDaysInt(millis: Long) : Int {
       return TimeUnit.MILLISECONDS.toDays(millis).toInt()
    }

    fun convertDaysToMillisecondsLong(days: Int) : Long {
        return  TimeUnit.DAYS.toMillis(days.toLong())
    }

    fun convertMillisecondsToHourOfDayInt(millis: Long) : Int {
        return TimeUnit.MILLISECONDS.toHours(millis).toInt()
    }

    fun convertMillisecondsToMinutesOfHourInt(millis: Long) : Int {
        return TimeUnit.MILLISECONDS.toMinutes(millis-(TimeUnit.MILLISECONDS.toHours(millis)*60*60*1000)).toInt()
    }




    fun epochDaysLocalNow() : Long {
        return LocalDate.now().toEpochDay()
    }

    fun localTimeNowInMilliseconds() : Long {
        return LocalTime.now().toKotlinLocalTime().toMillisecondOfDay().toLong()
        //TimeUnit.NANOSECONDS.toMillis(LocalTime.parse(convertMillisToHourOfDay(System.currentTimeMillis())).toNanoOfDay())
    }

    fun systemCurrentTimeInMillis() : Long{
        return System.currentTimeMillis()
    }


    fun convertMillisToHourOfDay(milisec: Long) : String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milisec



        var mH = calendar[Calendar.HOUR_OF_DAY]
        if(mH==-1) mH = 23

        val mm = calendar[Calendar.MINUTE]

        return "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"

    }
    fun convertMillisToDayOfWeek(milli: Long): Int {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milli

        return calendar[Calendar.DAY_OF_WEEK]
    }
    fun convertMillisToWeekNumber(millis: Long) : String {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis

        val mYear = calendar[Calendar.YEAR]
        val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

        val mDay = calendar[Calendar.DAY_OF_MONTH]
        val mWeek = calendar[Calendar.WEEK_OF_YEAR]

        return "$mWeek"

    }
}