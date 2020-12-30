package com.abhriya.callblockr.di

import android.app.Application
import android.content.Context
import com.abhriya.callblockr.NotificationProvider
import com.abhriya.callblockr.NotificationsProviderImpl
import com.abhriya.callblockr.PhoneReceiver
import com.abhriya.callblockr.PhoneReceiverImpl
import com.abhriya.callblockr.calllogprovider.CallLogProvider
import com.abhriya.callblockr.calllogprovider.CallLogProviderImpl
import com.abhriya.callblockr.contactsprovider.ContactsProvider
import com.abhriya.callblockr.contactsprovider.ContactsProviderImpl
import com.abhriya.callblockr.data.ContactsRepository
import com.abhriya.callblockr.data.ContactsRepositoryImpl
import com.abhriya.datasource.local.LocalDataSource
import com.abhriya.commons.SystemPermissionUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesContactRepository(
        localDataSource: LocalDataSource,
        contactsProvider: ContactsProvider,
        callLogProvider: CallLogProvider
    ): ContactsRepository =
        ContactsRepositoryImpl(localDataSource, contactsProvider, callLogProvider)

    @Provides
    @Singleton
    fun providesPhoneReceiver(
        localDataSource: LocalDataSource,
        notificationProvider: NotificationProvider
    ): PhoneReceiver = PhoneReceiverImpl(localDataSource, notificationProvider)

    @Provides
    @Singleton
    fun providesNotificationsProvider(systemPermissionUtil: SystemPermissionUtil): NotificationProvider = NotificationsProviderImpl(systemPermissionUtil)

    @Provides
    @Singleton
    fun providesContactProvider(
        @ApplicationContext context: Context,
        permissionUtil: SystemPermissionUtil
    ): ContactsProvider = ContactsProviderImpl(context, permissionUtil)

    @Provides
    @Singleton
    fun providesCallLogProvider(
        @ApplicationContext context: Context,
        systemPermissionUtil: SystemPermissionUtil
    ): CallLogProvider = CallLogProviderImpl(context, systemPermissionUtil)

}