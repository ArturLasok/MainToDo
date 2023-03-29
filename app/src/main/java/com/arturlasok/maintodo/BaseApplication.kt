package com.arturlasok.maintodo

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Configuration
import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.ItemDao
import com.arturlasok.maintodo.cache.model.CategoryToDoEntity
import com.arturlasok.maintodo.cache.model.ItemToDoEntity
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.domain.model.ItemToDo
import com.arturlasok.maintodo.interactors.RoomInter
import com.arturlasok.maintodo.interactors.util.RoomDataState
import com.arturlasok.maintodo.interactors.util.convertMillisToHourFromGmt
import com.arturlasok.maintodo.ui.start_screen.StartViewModel
import com.arturlasok.maintodo.util.AlarmReceiver
import com.arturlasok.maintodo.util.TAG
import com.arturlasok.maintodo.util.millisToDateAndHour
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import java.time.Clock
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
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
                item.dItemRemindTime,
                item.dItemDeliveryTime,
                item.dItemTitle,
                item.dItemDescription,
                item.dItemToken,
                item.dItemId ?: 0
            )
        }
    )


    }

    private fun makeAlarm(alarmTime: Long, taskName: String, taskDesc: String,taskToken: String, taskId: Long) {
        Log.i(TAG,"ALARM TIME ZONE OFFSET(s): ${ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds}")
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
        val clockInfo = AlarmManager.AlarmClockInfo(alarmTime-((ZoneId.systemDefault().rules.getOffset(
            Instant.now()).totalSeconds)*1000), basicPendingIntent)
        alarmManager.setAlarmClock(clockInfo, pendingIntent)
    }
    //ALARMS
    private fun addAlarm(time: Long, beganTime: Long, name: String, desc:String, token: String, taskId: Long) {
        //if remind time is before now no alarm!!!
        //add remind time if remind is after now() ( 1x ALARM )
        if(time-((ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds)*1000)>System.currentTimeMillis()) {

            Log.i(TAG,"REMIND ALARM alarm remind${convertMillisToHourFromGmt(time)} Task name $name, token $token, id: ${taskId.unaryMinus()}")
            makeAlarm(time,name,desc,token,taskId.unaryMinus())

        }
        //add alarm when task began is after now() ( 1x ALARM )
        if(beganTime-((ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds)*1000)>System.currentTimeMillis()){

            Log.i(TAG,"DELIVERY BEGAN ALARM  beganTime GMT: ${convertMillisToHourFromGmt(beganTime)} Task name $name, token $token, id: $taskId ")
            makeAlarm(beganTime,name,desc,token,taskId)

        }
        else {
            // no action
        }

    }



}