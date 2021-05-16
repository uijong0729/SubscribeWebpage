package com.example.subscribewebpage.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.subscribewebpage.ItemDetailHostActivity
import com.example.subscribewebpage.R

object AppNotification {

    fun createNotification(
        context:Context,
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
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, ItemDetailHostActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        var builder = NotificationCompat.Builder(context, Const.NOTIFICATION_CHANNEL_ID).apply {
            this.priority = NotificationCompat.PRIORITY_DEFAULT
            this.setSmallIcon(R.drawable.ic_launcher_foreground)
            this.setContentTitle(title)
            this.setContentText(des)
            this.setContentIntent(pendingIntent)
            this.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }
    }
}