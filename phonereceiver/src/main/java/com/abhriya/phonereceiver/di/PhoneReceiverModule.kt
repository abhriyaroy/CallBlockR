package com.abhriya.phonereceiver.di

import com.abhriya.database.DatabaseHelper
import com.abhriya.phonereceiver.PhoneReceiver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PhoneReceiverModule {

    @Provides
    @Singleton
    fun providesPhoneReceiver(databaseHelper: DatabaseHelper): PhoneReceiver =
        PhoneReceiver(databaseHelper)
}