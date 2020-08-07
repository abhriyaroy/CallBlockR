package com.abhriya.callblocker.di

import android.app.Application
import android.content.Context
import com.abhriya.callblocker.data.ContactsRepository
import com.abhriya.callblocker.data.ContactsRepositoryImpl
import com.abhriya.database.DatabaseHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesContactRepository(databaseHelper: DatabaseHelper): ContactsRepository =
        ContactsRepositoryImpl(databaseHelper)
}