package com.abhriya.callblocker.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import com.abhriya.callblocker.di.scopes.PerApplication

@Module
class AppModule {
    @Provides
    @PerApplication
    fun provideContext(application: Application): Context {
        return application
    }
}