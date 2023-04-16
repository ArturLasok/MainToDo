package com.arturlasok.maintodo.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.arturlasok.maintodo.BaseApplication
import com.arturlasok.maintodo.dataStore
import kotlinx.coroutines.runBlocking

class AcceptReceiver : BroadcastReceiver() {
    val CONFIRM_TASK = intPreferencesKey("confirm_task")

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("task_to_accept_id",0L).toInt()
        val close =  intent.getLongExtra("close_id",0L).toInt()

        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        Log.i(TAG,"maintodo take accept RECEIVED task ${taskId}  // close ${close}")
            if(taskId>0L) {


                manager.cancel(taskId)
                manager.cancel(taskId.unaryMinus())
           (context.applicationContext as BaseApplication).acceptTask(intent.getLongExtra("task_to_accept_id",0L))

                runBlocking {
                    context.dataStore.edit { settings->
                        settings[CONFIRM_TASK] = taskId

                    }
                }



            } else {

                manager.cancel(close)
                manager.cancel(close.unaryMinus())
            }

    }
}