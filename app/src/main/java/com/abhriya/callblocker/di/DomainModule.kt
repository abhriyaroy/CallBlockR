package com.abhriya.callblocker.di

import com.abhriya.callblocker.data.ContactsRepository
import com.abhriya.callblocker.domain.ContactsInteractor
import com.abhriya.callblocker.domain.ContactsUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun providesContactsUseCase(contactsRepository: ContactsRepository): ContactsUseCase =
        ContactsInteractor(contactsRepository)
}