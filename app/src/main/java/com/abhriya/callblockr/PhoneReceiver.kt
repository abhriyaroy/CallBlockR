package com.abhriya.callblockr

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.abhriya.commons.util.stringRes
import com.abhriya.datasource.local.LocalDataSource
import com.android.internal.telephony.ITelephony
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


interface PhoneReceiver {
    fun onPhoneStateChange(
        context: Context,
        intent: Intent,
        onNotificationClickIntent: Intent
    )
}

const val ITELEPHONY_METHOD = "getITelephony"

class PhoneReceiverImpl(
    private val localDataSource: LocalDataSource,
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
            val contactData = localDataSource.getBlockedContactByNumber(incomingNumber)
            return contactData != null
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private suspend fun rejectCall(
        @NonNull context: Context,
        intent: Intent,
        onNotificationClickIntent: Intent
    ) {
        if (!ALREADY_ON_CALL) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                try {
                    val telecomManager = Objects.requireNonNull(
                        context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                    )
                    if (telecomManagerEndCall(telecomManager)) {
                        Log.i(this.javaClass.name, "endCall() ended call using TelecomManager")
                    } else {
                        Log.w(this.javaClass.name,"endCall() TelecomManager returned false")
                    }
                } catch (e: Exception) {
                    Log.w(this.javaClass.name, "endCall() error while ending call with TelecomManager", e)
                }
            } else {
                try {
                    val tm = Objects.requireNonNull(
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    )
                    val m = tm.javaClass.getDeclaredMethod(ITELEPHONY_METHOD)
                    m.isAccessible = true
                    val telephony =
                        Objects.requireNonNull(m.invoke(tm) as ITelephony)
                    if (telephony.endCall()) {
                        Log.i(this.javaClass.name,"endCall() ended call using ITelephony")
                    } else {
                        Log.w(this.javaClass.name,"endCall() ITelephony returned false")
                    }
                } catch (e: Exception) {
                    Log.w(this.javaClass.name, "endCall() error while ending call with ITelephony", e)
                }
            }
        }
        val notificationBodyIdentifier: String? =
            localDataSource.getBlockedContactByNumber(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER))?.name
                ?: intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        notificationProvider.showCallBlockNotification(
            context,
            onNotificationClickIntent,
            notificationBodyIdentifier ?: context.stringRes(R.string.unknown)
        )
    }

    // no choice
    @SuppressLint("MissingPermission") // maybe shouldn't
    @RequiresApi(Build.VERSION_CODES.P)
    private fun telecomManagerEndCall(telecomManager: TelecomManager): Boolean {
        return telecomManager.endCall()
    }


    companion object {
        private var ALREADY_ON_CALL = false
    }

}