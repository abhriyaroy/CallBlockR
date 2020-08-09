package com.abhriya.notificationsprovider

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abhriya.commons.util.stringRes

interface NotificationProvider {
    fun showCallBlockNotification(
        context: Context,
        onNotificationClickIntent: Intent,
        number: String
    )
}

const val NOTIFICATION_ID = "notif_id"
const val NOTIFICATION_CHANNEL_DESCRIPTION = "Call blocked"
const val NOTIFICATION_CHANNEL_ID = "call_blocker_default_channel"
const val DEFAULT_CHANNEL_ID = "default"

class NotificationsProviderImpl : NotificationProvider{

    override fun showCallBlockNotification(
        context: Context,
        onNotificationClickIntent: Intent,
        number: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                NOTIFICATION_ID,
                context.stringRes(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = NOTIFICATION_CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
        val notify: Notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_small)
            .setContentTitle(context.stringRes(R.string.call_blocked))
            .setContentText(context.getString(R.string.call_rejected_from, number))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setShowWhen(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    onNotificationClickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .addPerson("tel:$number")
            .setGroup("rejected")
            .setChannelId(DEFAULT_CHANNEL_ID)
            .setGroupSummary(true)
            .build()
        NotificationManagerCompat.from(context)
            .notify("private", NOTIFY_REJECTED, notify)
    }

    companion object{
        private const val NOTIFY_REJECTED = 0

    }

}