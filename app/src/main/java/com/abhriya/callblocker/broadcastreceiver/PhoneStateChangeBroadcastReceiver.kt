package com.abhriya.callblocker.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.abhriya.phonereceiver.PhoneReceiver
import dagger.android.AndroidInjection
import javax.inject.Inject

class PhoneStateChangeBroadcastReceiver : BroadcastReceiver() {

    @Inject
    internal lateinit var phoneReceiver: PhoneReceiver

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        phoneReceiver.onPhoneStateChange(context, intent)
    }
}