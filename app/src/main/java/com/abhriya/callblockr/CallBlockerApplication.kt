package com.abhriya.callblockr

import android.app.Application
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CallBlockerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }

}