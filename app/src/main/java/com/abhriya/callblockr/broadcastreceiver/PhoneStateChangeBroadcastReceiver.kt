package com.abhriya.callblockr.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.abhriya.callblockr.ui.MainActivity
import com.abhriya.callblockr.PhoneReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhoneStateChangeBroadcastReceiver : BroadcastReceiver() {

    @Inject
    internal lateinit var phoneReceiver: PhoneReceiver

    override fun onReceive(context: Context, intent: Intent) {
        phoneReceiver.onPhoneStateChange(context, intent, Intent(context, MainActivity::class.java))
    }
}