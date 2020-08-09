package com.abhriya.callblocker.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.abhriya.callblocker.ui.MainActivity
import com.abhriya.notificationsprovider.NotificationProvider
import dagger.android.AndroidInjection
import javax.inject.Inject

class ForegroundKeepAppAliveService : Service() {

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    @Inject
    internal lateinit var notificationProvider: NotificationProvider

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not supported");
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationProvider.showKeepAppAliveForegroundNotification(
            this,
            Intent(this, MainActivity::class.java)
        )
        return super.onStartCommand(intent, flags, startId)
    }


}