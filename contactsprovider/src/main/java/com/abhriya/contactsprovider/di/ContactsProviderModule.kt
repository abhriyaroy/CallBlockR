package com.abhriya.contactsprovider.di

import android.content.Context
import com.abhriya.contactsprovider.ContactsProvider
import com.abhriya.contactsprovider.ContactsProviderImpl
import com.abhriya.systempermissions.SystemPermissionsHandler
import com.abhriya.systempermissions.di.SystemPermissionModule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
class ContactsProviderModule {

    @Singleton
    @Provides
    fun providesContactsProvider(context: Context): ContactsProvider = ContactsProviderImpl(context)
}