package com.abhriya.phonereceiver

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.abhriya.commons.util.stringRes
import com.abhriya.blockedcontactsdatabase.DatabaseHelper
import com.abhriya.notificationsprovider.NotificationProvider
import com.android.internal.telephony.ITelephony
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface PhoneReceiver {
    fun onPhoneStateChange(
        context: Context,
        intent: Intent,
        onNotificationClickIntent: Intent
    )
}

const val ITELEPHONY_METHOD = "getITelephony"

class PhoneReceiverImpl(
    private val databaseHelper: DatabaseHelper,
    private val notificationProvider: NotificationProvider
) : PhoneReceiver {

    override fun onPhoneStateChange(
        context: Context,
        intent: Intent,
        onNotificationClickIntent: Intent
    ) {
        GlobalScope.launch {
            if (TelephonyManager.ACTION_PHONE_STATE_CHANGED == intent.action && intent.getStringExtra(
                    TelephonyManager.EXTRA_STATE
                ) == TelephonyManager.EXTRA_STATE_RINGING && shouldRejectPhoneCall(intent)
            ) {
                rejectCall(context, intent, onNotificationClickIntent)
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
            println("contact data obtained $contactData")
            return contactData != null
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun rejectCall(
        @NonNull context: Context,
        intent: Intent,
        onNotificationClickIntent: Intent
    ) {
        if (!ALREADY_ON_CALL) {
            val endedCallSuccessFully =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    endCallOnApi28AndAbove(context)
                } else {
                    endCallOnApiPre28(context)
                }
            if (!endedCallSuccessFully) {
                // Handle accordingly
            }
        }
        notificationProvider.showCallBlockNotification(
            context,
            onNotificationClickIntent,
            intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                ?: context.stringRes(R.string.unknown)
        )
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.P)
    private fun endCallOnApi28AndAbove(context: Context): Boolean {
        val telecomManager =
            context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        return try {
            telecomManager.endCall()
            true
        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun endCallOnApiPre28(context: Context): Boolean {
        val tm =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return try {
            val m =
                tm.javaClass.getDeclaredMethod(ITELEPHONY_METHOD)
            m.isAccessible = true
            val telephony: ITelephony = m.invoke(tm) as ITelephony
            telephony.endCall()
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private var ALREADY_ON_CALL = false
    }

}