package com.abhriya.blockedcontacts.di

import com.abhriya.blockedcontacts.data.ContactsRepository
import com.abhriya.blockedcontacts.data.ContactsRepositoryImpl
import com.abhriya.callblocker.domain.ContactsInteractor
import com.abhriya.callblocker.domain.ContactsUseCase
import com.abhriya.commons.contactsprovider.ContactsProvider
import com.abhriya.datasource.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class BlockedContactsModule {
    @Singleton
    @Provides
    fun providesBlockedContactsRepository(
        localDataSource: LocalDataSource,
        contactsProvider: ContactsProvider
    ): ContactsRepository =
        ContactsRepositoryImpl(
            localDataSource,
            contactsProvider
        )

    @Singleton
    @Provides
    fun providesBlockedContactsUseCase(
        contactsRepository: ContactsRepository
    ): ContactsUseCase = ContactsInteractor(contactsRepository)
}