package com.arturlasok.maintodo

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.util.MainTimeDate
import com.arturlasok.maintodo.interactors.util.MainTimeDate.convertMillisToHourFromGmt
import com.arturlasok.maintodo.util.AlarmReceiver
import com.arturlasok.maintodo.util.TAG
import com.arturlasok.maintodo.util.millisToDateAndHour
import dagger.hilt.android.HiltAndroidApp
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(),Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    @Inject lateinit var workerViewModel: WorkerViewModel
    fun reScheduleAlarms() {

    workerViewModel.rescheduleAllAlarms(
        addAlarm = { item->
            addAlarm(
                item.dItemRemindTime+ MainTimeDate.utcTimeZoneOffsetMillis(millisToDateAndHour(item.dItemRemindTime)),
                item.dItemDeliveryTime+ MainTimeDate.utcTimeZoneOffsetMillis(millisToDateAndHour(item.dItemDeliveryTime)),
                item.dItemTitle,
                item.dItemDescription,
                item.dItemToken,
                item.dItemId ?: 0
            )
        }
    )


    }

    private fun makeAlarm(alarmTime: Long, taskName: String, taskDesc: String,taskToken: String, taskId: Long) {

        val taskInfo = ItemToDo(dItemTitle = taskName, dItemId = taskId, dItemToken = taskToken, dItemDescription = taskDesc)
        // creating alarmManager instance
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // adding intent and pending intent to go to AlarmReceiver Class in future
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("task_info", taskInfo)
        val pendingIntent = PendingIntent.getBroadcast(this, (taskInfo.dItemId?.toInt()?: 0),  intent, PendingIntent.FLAG_IMMUTABLE)
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val basicPendingIntent = PendingIntent.getActivity(this, (taskInfo.dItemId?.toInt()?: 0), mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
        // creating clockInfo instance
        val clockInfo = AlarmManager.AlarmClockInfo(alarmTime, basicPendingIntent)
        alarmManager.setAlarmClock(clockInfo, pendingIntent)
    }
    //ALARMS
    private fun addAlarm(time: Long, beganTime: Long, name: String, desc:String, token: String, taskId: Long) {

        if(time>MainTimeDate.systemCurrentTimeInMillis()) {
            Log.i(TAG,"REMIND ALARM alarm remind${convertMillisToHourFromGmt(time)} Task name $name, token $token, id: ${taskId.unaryMinus()}")
            makeAlarm(time,name,desc,token,taskId.unaryMinus())

        }
        if(beganTime>MainTimeDate.systemCurrentTimeInMillis()){

            makeAlarm(beganTime,name,desc,token,taskId)

        }
        else {
            // no action
        }

    }



}