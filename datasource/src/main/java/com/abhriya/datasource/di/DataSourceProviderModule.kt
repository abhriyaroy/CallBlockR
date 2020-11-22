package com.abhriya.datasource.di

import android.content.Context
import com.abhriya.datasource.local.LocalDataSource
import com.abhriya.datasource.local.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DataSourceProviderModule {

    @Singleton
    @Provides
    fun providesLocalDataSource(applicationContext: Context): LocalDataSource =
        LocalDataSourceImpl(applicationContext)
}