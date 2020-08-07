package com.abhriya.commons.di

import com.abhriya.commons.DialogHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonsProviderModule {

    @Singleton
    @Provides
    fun providesDialogHelper() : DialogHelper = DialogHelper()
}