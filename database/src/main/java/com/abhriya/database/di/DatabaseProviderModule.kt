package com.abhriya.database.di

import android.content.Context
import com.abhriya.database.DatabaseHelper
import com.abhriya.database.DatabaseHelperImpl
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