package com.abhriya.callblockr.di

import com.abhriya.callblockr.data.repository.ContactsRepository
import com.abhriya.callblockr.domain.ContactsInteractor
import com.abhriya.callblockr.domain.ContactsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DomainModule {

    @Provides
    @Singleton
    fun providesContactsUseCase(contactsRepository: ContactsRepository): ContactsUseCase =
        ContactsInteractor(contactsRepository)
}