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
import androidx.core.app.NotificationCompat.Action
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

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)

        val positiveItemId = if((task?.dItemId?: 0L) < 0L) {
            task?.dItemId?.unaryMinus()
        } else { task?.dItemId };
        val negativeItemId = (if((task?.dItemId?: 0L) < 0L) {
            task?.dItemId
        } else { task?.dItemId?.unaryMinus() })?.toInt()
        Log.i(TAG,">>>> task alarm received ${task?.dItemTitle ?: "null title"} // normal:: ${task?.dItemId}// positiv:: $positiveItemId")

        val intent = Intent(p0, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        intent.putExtra("show_task", positiveItemId)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(p0, 0, intent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
        val acceptIntent = Intent(p0,AcceptReceiver::class.java).apply {
            putExtra("task_to_accept_id", positiveItemId)
        }
        val closeIntent = Intent(p0,AcceptReceiver::class.java).apply {
            putExtra("close_id",  task?.dItemId)
        }

        val acceptPendingIntent = PendingIntent.getBroadcast(p0,(task?.dItemId?.toInt()?: 0),acceptIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
        val closePendingIntent = PendingIntent.getBroadcast(p0,negativeItemId?: 0,closeIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)


        if((task?.dItemId ?: 0L) > 0L) {
        val builder = NotificationCompat.Builder(p0, channelId)
                .setContentTitle(
                    if(task?.dItemId!! <0) { UiText.StringResource(R.string.remindetask,"asd").asString(p0.applicationContext) } else { UiText.StringResource(R.string.taskfornow,"asd").asString(p0.applicationContext) })
                .setContentText(task?.dItemTitle)
                .setSmallIcon(R.drawable.all_dark)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .addAction(R.drawable.bin_dark,"OZNACZ JAKO WYKONANE",acceptPendingIntent)
                .addAction(R.drawable.more,"ZAMKNIJ",closePendingIntent)
                 manager.notify(task?.dItemId?.toInt() ?: 0,builder.build())
                task.dItemId.unaryMinus()?.let { manager.cancel(it.toInt()); Log.i(TAG,"task notification close $it") }
        }
        else
        {
            val builder = NotificationCompat.Builder(p0, channelId)
                .setContentTitle(
                    if(task?.dItemId!! <0) { UiText.StringResource(R.string.remindetask,"asd").asString(p0.applicationContext) } else { UiText.StringResource(R.string.taskfornow,"asd").asString(p0.applicationContext) })
                .setContentText(task?.dItemTitle)
                .setSmallIcon(R.drawable.all_dark)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent).setAutoCancel(true)
                //.addAction(R.drawable.bin_dark,"OZNACZ JAKO WYKONANE",acceptPendingIntent)
                .addAction(R.drawable.more,"ZAMKNIJ",closePendingIntent)
            manager.notify(task?.dItemId?.toInt() ?: 0,builder.build())
        }

    }
}