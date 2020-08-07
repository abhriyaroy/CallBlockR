package com.abhriya.callblocker.di

import com.abhriya.callblocker.broadcastreceiver.PhoneStateChangeBroadcastReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {
    @ContributesAndroidInjector
    abstract fun contributesPhoneStateChangeBroadcastReceiver() : PhoneStateChangeBroadcastReceiver
}