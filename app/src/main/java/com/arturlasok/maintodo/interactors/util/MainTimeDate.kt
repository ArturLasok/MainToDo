package com.arturlasok.maintodo.interactors.util

import com.arturlasok.maintodo.util.millisToDateAndHour
import kotlinx.datetime.toKotlinLocalTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

object MainTimeDate {
fun convertMillisToHourFromGmt(milisec: Long) : String {
   // val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
    val calendar = Calendar.getInstance()

    calendar.timeInMillis = milisec

    calendar.timeZone.id = "GMT"

    val mYear = calendar[Calendar.YEAR]
    val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

    val mDay = calendar[Calendar.DAY_OF_MONTH]

    val mPm = calendar[Calendar.AM_PM]
    val mHPm = calendar[Calendar.HOUR]

    var mH = calendar[Calendar.HOUR_OF_DAY]
    //if(mH==-1) mH = 23
    val ss = calendar[Calendar.SECOND]
    val mm = calendar[Calendar.MINUTE]

    //return "$mHPm:$mm, $mPm"
    return "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm:${if(ss<10) "0" else ""}$ss $mDay/$mMonth/$mYear"
}
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

    fun utcTimeZoneOffsetMillis(atDate: String) : Long {
        //Log.i(TAG,"alarm zone offset: ${((ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds)*1000).toLong()}")
        //Log.i(TAG,"alarm zone offset: ${((ZoneId.systemDefault().rules.getOffset(LocalDateTime.parse(atDate)).totalSeconds)*1000).toLong()}")
        return -((ZoneId.systemDefault().rules.getOffset(LocalDateTime.parse(atDate)).totalSeconds)*1000).toLong()
       // Log.i(TAG,"alarm zone offset: ${(TimeUnit.NANOSECONDS.toMillis(LocalTime.now(Clock.system(ZoneId.of("UTC"))).toNanoOfDay())-TimeUnit.NANOSECONDS.toMillis(LocalTime.now(Clock.systemDefaultZone()).toNanoOfDay()))}")
       // return (TimeUnit.NANOSECONDS.toMillis(LocalTime.now(Clock.system(ZoneId.of("UTC"))).toNanoOfDay())-TimeUnit.NANOSECONDS.toMillis(LocalTime.now(Clock.systemDefaultZone()).toNanoOfDay()))
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
       // val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        calendar.timeInMillis = milli

        return calendar[Calendar.DAY_OF_WEEK]
    }
    fun convertMillisToWeekNumber(millis: Long) : String {
        val calendar = Calendar.getInstance()
       // val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        calendar.timeInMillis = millis

        val mYear = calendar[Calendar.YEAR]
        val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

        val mDay = calendar[Calendar.DAY_OF_MONTH]
        val mWeek = calendar[Calendar.WEEK_OF_YEAR]

        return "$mWeek"

    }
    fun localFormDate(milisec: Long) : String {

        val calendar = Calendar.getInstance()

        calendar.timeInMillis = milisec

        calendar.timeZone.id = "GMT"

        val mYear = calendar[Calendar.YEAR]
        val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

        val mDay = calendar[Calendar.DAY_OF_MONTH]


        var mH = calendar[Calendar.HOUR_OF_DAY]
        //if(mH==-1) mH = 23
        val ss = calendar[Calendar.SECOND]
        val mm = calendar[Calendar.MINUTE]
        //:${if(ss<10) "0" else ""}$ss"
        val toparseDate = "${mYear}-${mMonth}-${if(mDay<10) "0" else ""}${mDay}"
        val date = LocalDate.parse(toparseDate).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        return date.toString()

    }
    fun localFormTimeFromString(text: String) : String {

        val time = LocalTime.parse(text).truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        return time.toString()

    }
    fun localFormTime(milisec: Long) : String {

        val calendar = Calendar.getInstance()

        calendar.timeInMillis = milisec

        calendar.timeZone.id = "GMT"

        val mYear = calendar[Calendar.YEAR]
        val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

        val mDay = calendar[Calendar.DAY_OF_MONTH]

        var mH = calendar[Calendar.HOUR_OF_DAY]
        val ss = calendar[Calendar.SECOND]
        val mm = calendar[Calendar.MINUTE]
        //:${if(ss<10) "0" else ""}$ss"
        val toparseDate = "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"
        val time = LocalTime.parse(toparseDate).truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        return time.toString()
        //return "$mHPm:$mm, $mPm"
        //return "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm $mDay/$mMonth/$mYear"
    }
    fun localFormDateAndTime(milisec: Long) : String {

        val calendar = Calendar.getInstance()

        calendar.timeInMillis = milisec

        calendar.timeZone.id = "GMT"

        val mYear = calendar[Calendar.YEAR]
        val mMonth = if((calendar[Calendar.MONTH]+1)<10) { "0"+(calendar[Calendar.MONTH]+1) } else { calendar[Calendar.MONTH]+1 }

        val mDay = calendar[Calendar.DAY_OF_MONTH]

        var mH = calendar[Calendar.HOUR_OF_DAY]
        val ss = calendar[Calendar.SECOND]
        val mm = calendar[Calendar.MINUTE]
        //:${if(ss<10) "0" else ""}$ss"
        val toparseDate = "${mYear}-${mMonth}-${if(mDay<10) "0" else ""}${mDay}"+"T${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm"
       val dateTime = LocalDateTime.parse(toparseDate).truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM,FormatStyle.SHORT))
        return dateTime.toString()
        //return "$mHPm:$mm, $mPm"
        //return "${if(mH<10) "0" else "" }$mH:${if(mm<10) "0" else ""}$mm $mDay/$mMonth/$mYear"
    }
    fun dateIs24h() : Boolean {
        val time = millisToDateAndHour(System.currentTimeMillis())
        val local = LocalDateTime.parse(time).truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM,FormatStyle.SHORT))
        return !(local.contains("AM") || local.contains("PM"))
    }


}