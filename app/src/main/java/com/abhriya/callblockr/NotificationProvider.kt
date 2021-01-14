package com.abhriya.callblockr

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abhriya.commons.SystemPermissionUtil
import com.abhriya.commons.util.stringRes

interface NotificationProvider {
    fun showCallBlockNotification(
        context: Context,
        onNotificationClickIntent: Intent,
        number: String
    )

    fun showKeepAppAliveForegroundNotification(
        context: Context,
        service: Service,
        onNotificationClickIntent: Intent
    )
}

const val NOTIFICATION_ID = "notif_id"
const val FOREGROUND_NOTIFICATION_ID = "foreground_notif_id"
const val NOTIFICATION_CHANNEL_DESCRIPTION = "Call blocked"
const val NOTIFICATION_CHANNEL_ID = "call_blocker_defaul_channel"
const val DEFAULT_CHANNEL_GROUP = "defaul_group"

class NotificationsProviderImpl(private val systemPermissionUtil: SystemPermissionUtil) :
    NotificationProvider {

    private var serviceKeepAliveDescriptiontext = ""

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
            .setSmallIcon(R.drawable.ic_notification)
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
            .setGroup(DEFAULT_CHANNEL_GROUP)
            .setChannelId(NOTIFICATION_ID)
            .setGroupSummary(true)
            .build()
        NotificationManagerCompat.from(context)
            .notify("private", NOTIFY_REJECTED, notify)
    }

    override fun showKeepAppAliveForegroundNotification(
        context: Context,
        service: Service,
        onNotificationClickIntent: Intent
    ) {
        val contentText = if (systemPermissionUtil.getMissingPermissionsArray(
                systemPermissionUtil.checkPermissions(
                    context,
                    getListOfRequiredPermissions()
                )
            ).isNotEmpty()
        ) {
            service.stringRes(R.string.call_blocker_not_running_due_to_missing_permission)
        } else {
            service.stringRes(R.string.call_blocker_running)
        }
        if (contentText != serviceKeepAliveDescriptiontext) {
            serviceKeepAliveDescriptiontext = contentText
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    FOREGROUND_NOTIFICATION_ID,
                    service.stringRes(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = NOTIFICATION_CHANNEL_DESCRIPTION
                notificationManager.createNotificationChannel(channel)
            }

            val notify: Notification = NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(service.stringRes(R.string.app_name))
                .setContentText(serviceKeepAliveDescriptiontext)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setShowWhen(true)
                .setContentIntent(
                    PendingIntent.getActivity(
                        service,
                        0,
                        onNotificationClickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setChannelId(FOREGROUND_NOTIFICATION_ID)
                .build()

            service.startForeground(1013372, notify)
        }
    }

    companion object {
        private const val NOTIFY_REJECTED = 0

    }

    private fun getListOfRequiredPermissions(): List<String> {
        val requiredPermissions: MutableList<String> =
            mutableListOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE
//                Manifest.permission.READ_CALL_LOG
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requiredPermissions.add(Manifest.permission.ANSWER_PHONE_CALLS)
        }
        return requiredPermissions
    }

}