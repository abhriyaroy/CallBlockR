package com.abhriya.callblocker

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDex
import com.abhriya.callblocker.di.DaggerAppComponent
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class CallBlockerApplication : Application(), HasActivityInjector,
    HasServiceInjector, HasBroadcastReceiverInjector {

    @Inject
    internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    internal lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

    @Inject
    internal lateinit var broadcastDispatchingAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        initDagger()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    override fun serviceInjector() = serviceDispatchingAndroidInjector

    override fun broadcastReceiverInjector() = broadcastDispatchingAndroidInjector

    private fun initDagger() {
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}