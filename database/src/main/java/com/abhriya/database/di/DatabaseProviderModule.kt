package com.abhriya.database.di

import android.content.Context
import androidx.room.Room
import com.abhriya.database.DatabaseHelper
import com.abhriya.database.blockedcontacts.BlockedContactsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseProviderModule {

    @Singleton
    @Provides
    fun providesDatabase(applicationContext: Context): DatabaseHelper = DatabaseHelper(applicationContext)
}