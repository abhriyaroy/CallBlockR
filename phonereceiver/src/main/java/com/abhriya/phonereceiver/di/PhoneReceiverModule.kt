package com.abhriya.phonereceiver.di

import com.abhriya.database.DatabaseHelper
import com.abhriya.notificationsprovider.NotificationProvider
import com.abhriya.phonereceiver.PhoneReceiver
import com.abhriya.phonereceiver.PhoneReceiverImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PhoneReceiverModule {

    @Provides
    @Singleton
    fun providesPhoneReceiver(
        databaseHelper: DatabaseHelper,
        notificationProvider: NotificationProvider
    ): PhoneReceiver =
        PhoneReceiverImpl(databaseHelper, notificationProvider)
}