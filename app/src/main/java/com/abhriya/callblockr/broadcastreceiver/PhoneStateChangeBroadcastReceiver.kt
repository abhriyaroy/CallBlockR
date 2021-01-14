package com.abhriya.callblockr.broadcastreceiver

import android.content.Context
import android.content.Intent
import com.abhriya.callblockr.PhoneReceiver
import com.abhriya.callblockr.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhoneStateChangeBroadcastReceiver : HiltBroadcastReceiver() {

    @Inject
    internal lateinit var phoneReceiver: PhoneReceiver

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        phoneReceiver.onPhoneStateChange(context, intent, Intent(context, MainActivity::class.java))
    }
}