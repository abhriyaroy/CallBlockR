package com.abhriya.callblocker

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import androidx.multidex.MultiDex
import com.abhriya.callblocker.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import javax.inject.Inject

class CallBlockerApplication : Application(), HasActivityInjector, HasBroadcastReceiverInjector {

    @Inject
    internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    internal lateinit var broadcastDispatchingAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        initDagger()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    override fun broadcastReceiverInjector() = broadcastDispatchingAndroidInjector

    private fun initDagger(){
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}