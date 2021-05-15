package com.example.subscribewebpage.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.subscribewebpage.ItemDetailHostActivity
import com.example.subscribewebpage.R

object AppNotification {

    fun createNotification(
        activity:AppCompatActivity,
        id: Int,
        title: String,
        des: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Const.NOTIFICATION_CHANNEL_ID, title, importance).apply {
                description = des
            }

            val notificationManager: NotificationManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create an explicit intent for an Activity in your app
        val intent = Intent(activity, ItemDetailHostActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(activity, 0, intent, 0)
        var builder = NotificationCompat.Builder(activity, Const.NOTIFICATION_CHANNEL_ID).apply {
            this.priority = NotificationCompat.PRIORITY_DEFAULT
            this.setSmallIcon(R.drawable.ic_launcher_foreground)
            this.setContentTitle(title)
            this.setContentText(des)
            this.setContentIntent(pendingIntent)
            this.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }

        with(NotificationManagerCompat.from(activity)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }
    }
}