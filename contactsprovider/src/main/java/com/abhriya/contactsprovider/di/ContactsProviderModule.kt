package com.abhriya.contactsprovider.di

import android.content.Context
import com.abhriya.contactsprovider.ContactsProvider
import com.abhriya.contactsprovider.ContactsProviderImpl
import com.abhriya.systempermissions.SystemPermissionUtil
import com.abhriya.systempermissions.SystemPermissionsHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContactsProviderModule {

    @Singleton
    @Provides
    fun providesContactsProvider(
        context: Context,
        permissionsHandler: SystemPermissionsHandler,
        permissionUtil: SystemPermissionUtil
    ): ContactsProvider = ContactsProviderImpl(context, permissionsHandler, permissionUtil)
}