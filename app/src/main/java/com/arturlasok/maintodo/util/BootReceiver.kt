package com.arturlasok.maintodo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.arturlasok.maintodo.BaseApplication

class BootReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.i(TAG,"maintodo BOOT RECEIVED !!!!!!!!!!!!!  ! ! ! ! ! !! ! ! ! !  ! ! ! ! ")
            (context.applicationContext as BaseApplication).reScheduleAlarms()
        }
    }
}