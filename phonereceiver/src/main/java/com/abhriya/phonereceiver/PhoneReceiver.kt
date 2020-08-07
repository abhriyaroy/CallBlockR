package com.abhriya.phonereceiver

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import com.abhriya.database.DatabaseHelper
import com.android.internal.telephony.ITelephony
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PhoneReceiver(private val databaseHelper: DatabaseHelper) {


    fun onPhoneStateChange(context: Context, intent: Intent) {

        println("received here call")
        GlobalScope.launch {
            if (TelephonyManager.ACTION_PHONE_STATE_CHANGED == intent.action && intent.getStringExtra(
                    TelephonyManager.EXTRA_STATE
                ) == TelephonyManager.EXTRA_STATE_RINGING && shouldRejectPhoneCall(intent)
            ) {
                rejectCall(context, null)
            }
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK
            ) {
                ALREADY_ON_CALL = true
            } else if (intent.getStringExtra(
                    TelephonyManager.EXTRA_STATE
                ) == TelephonyManager.EXTRA_STATE_IDLE
            ) {
                ALREADY_ON_CALL = false
            }
        }
    }

    private suspend fun shouldRejectPhoneCall(intent: Intent): Boolean {
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            ?: return false
        if (incomingNumber.isNotBlank()) {
            val contactData = databaseHelper.getBlockedContactByNumber(incomingNumber)
            return contactData != null
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun rejectCall(
        @NonNull context: Context,
        number: Number?
    ) {
        if (!ALREADY_ON_CALL) {
            var failed = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val telecomManager =
                    context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                try {
                    telecomManager.endCall()
                    Log.d(PhoneReceiver.TAG, "Invoked 'endCall' on TelecomManager")
                } catch (e: Exception) {
                    Log.e(PhoneReceiver.TAG, "Couldn't end call with TelecomManager", e)
                    failed = true
                }
            } else {
                val tm =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                try {
                    val m =
                        tm.javaClass.getDeclaredMethod("getITelephony")
                    m.isAccessible = true
                    val telephony: ITelephony = m.invoke(tm) as ITelephony
                    telephony.endCall()
                } catch (e: Exception) {
                    Log.e(
                        PhoneReceiver.TAG,
                        "Couldn't end call with TelephonyManager",
                        e
                    )
                    failed = true
                }
            }
            if (failed) {
                Toast.makeText(
                    context,
                    "unsupported blocking",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
//        if (Build.VERSION.SDK_INT >= 26) {
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val channel = NotificationChannel(
//                "default",
//                "Call blocker",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            channel.description = "call blocked"
//            notificationManager.createNotificationChannel(channel)
//        }
//        val notify: Notification = NotificationCompat.Builder(context, "M_CH_ID")
//            .setContentTitle("title")
//            .setContentText(
//                "rejected number man"
//            )
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setCategory(NotificationCompat.CATEGORY_CALL)
//            .setShowWhen(true)
//            .setAutoCancel(true)
////                .setContentIntent(
////                    PendingIntent.getActivity(
////                        context,
////                        0,
////                        Intent(context, BlacklistActivity::class.java),
////                        PendingIntent.FLAG_UPDATE_CURRENT
////                    )
////                )
//            .addPerson("tel:$number")
//            .setGroup("rejected")
//            .setChannelId("default")
//            .setGroupSummary(true) /* swy: fix notifications not appearing on kitkat: https://stackoverflow.com/a/37070917/674685 */
//            .build()
////            val tag = if (number != null) number.number else "private"
//        NotificationManagerCompat.from(context)
//            .notify("private", NOTIFY_REJECTED, notify)
    }

    companion object {
        private var ALREADY_ON_CALL = false
        private val NOTIFY_REJECTED = 0
        val TAG = "ags"
    }

}