package com.arturlasok.maintodo.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arturlasok.maintodo.MainActivity
import com.arturlasok.maintodo.R
import com.arturlasok.maintodo.domain.model.ItemToDo


class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null


    override fun onReceive(p0: Context?, p1: Intent?) {

        val manager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId="pl_channel"
        val channelName  = "pl_message"

        val task = p1?.getSerializableExtra("task_info") as? ItemToDo
        Log.i(TAG,"alarm received ${task?.dItemTitle ?: "null title" }")
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
        val intent = Intent(p0, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        intent.putExtra("show_task",task?.dItemId ?: 0)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(p0, 0, intent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(p0, channelId)
            .setContentTitle(
                if(task?.dItemId!! <0) { UiText.StringResource(R.string.remindetask,"asd").asString(p0.applicationContext) } else { UiText.StringResource(R.string.taskfornow,"asd").asString(p0.applicationContext) })
            .setContentText(task?.dItemTitle)
            .setSmallIcon(R.drawable.all_dark)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        manager.notify(task?.dItemId?.toInt() ?: 0,builder.build())
    }
}