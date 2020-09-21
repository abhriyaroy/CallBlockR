package com.abhriya.blockedcontactsdatabase.di

import android.content.Context
import com.abhriya.blockedcontactsdatabase.DatabaseHelper
import com.abhriya.blockedcontactsdatabase.DatabaseHelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseProviderModule {

    @Singleton
    @Provides
    fun providesDatabase(applicationContext: Context): DatabaseHelper =
        DatabaseHelperImpl(applicationContext)
}