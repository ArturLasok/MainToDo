package com.arturlasok.maintodo.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arturlasok.maintodo.MainActivity
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null


    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i(TAG,"alarm received")
        val manager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId="pl_channel"
        val channelName  = "pl_message"

        val task = p1?.getSerializableExtra("task_info") as? ItemToDo

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
        val intent = Intent(p0, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(p0, 0, intent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(p0, channelId)
            .setContentTitle(task?.dItemTitle)
            .setContentText(task?.dItemDescription)
            .setSmallIcon(R.drawable.all_dark)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        manager.notify(task?.dItemId?.toInt() ?: 0,builder.build())

        /*
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("to_do_list", "Tasks Notification Channel", importance).apply {
            description = "Notification for Tasks"
        }
        val notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)



        val taskInfo = p1?.getSerializableExtra("dItemTitle") as? ItemToDo
        // tapResultIntent gets executed when user taps the notification
        val tapResultIntent = Intent(p0, MainActivity::class.java)
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent = getActivity( p0,0,tapResultIntent,FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = p0?.let {
            NotificationCompat.Builder(it, "to_do_list")
                .setContentTitle("Task Reminder")
                .setContentText(taskInfo?.dItemTitle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }
        //notificationManager = p0?.let { NotificationManagerCompat.from(it) }
        notification?.let { taskInfo?.let { it1 -> notificationManager?.notify(it1.dItemId?.toInt() ?:0, it) } }

         */
    }
}