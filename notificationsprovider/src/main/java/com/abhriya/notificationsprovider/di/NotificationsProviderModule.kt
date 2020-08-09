package com.abhriya.notificationsprovider.di

import android.content.Context
import com.abhriya.notificationsprovider.NotificationProvider
import com.abhriya.notificationsprovider.NotificationsProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationsProviderModule {

    @Singleton
    @Provides
    fun providesDatabase(applicationContext: Context): NotificationProvider =
        NotificationsProviderImpl()
}