package com.abhriya.contactsprovider.di

import com.abhriya.contactsprovider.ContactsProvider
import com.abhriya.systempermissions.SystemPermissionsHandler
import com.abhriya.systempermissions.di.SystemPermissionModule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class ContactsProviderModule {

    @Singleton
    @ContributesAndroidInjector(modules = [(SystemPermissionModule::class)])
    abstract fun providesContactsProvider(): ContactsProvider
}