package com.abhriya.blockedcontacts.data

import com.abhriya.blockedcontacts.data.entity.ContactEntity
import com.abhriya.callblocker.data.exception.DataLayerException
import com.abhriya.callblocker.data.mapper.ContactEntityMapper
import com.abhriya.commons.contactsprovider.ContactsProvider
import com.abhriya.datasource.local.LocalDataSource
import com.abhriya.datasource.local.exception.DatabaseException

interface ContactsRepository {
    suspend fun saveBlockedContact(contactEntity: ContactEntity)
    suspend fun unBlockContact(contactEntity: ContactEntity)
    suspend fun getAllBlockedContacts(): List<ContactEntity>
    suspend fun getAllContactsFromDevice(): List<ContactEntity>
}

class ContactsRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val contactsProvider: ContactsProvider
) : ContactsRepository {
    override suspend fun saveBlockedContact(contactEntity: ContactEntity) {
        try {
            with(ContactEntityMapper.mapToDbContactEntityFromContactEntity(contactEntity)) {
                localDataSource.saveToBlockedContactsDb(listOf(this))
            }
        } catch (databaseException: DatabaseException) {
            throw DataLayerException(databaseException.message)
        }
    }

    override suspend fun unBlockContact(contactEntity: ContactEntity) {
        try {
            with(ContactEntityMapper.mapToDbContactEntityFromContactEntity(contactEntity)) {
                localDataSource.removeBlockedContactsFromDb(listOf(this))
            }
        } catch (databaseException: DatabaseException) {
            throw DataLayerException(databaseException.message)
        }
    }

    override suspend fun getAllBlockedContacts(): List<ContactEntity> {
        try {
            return localDataSource.getAllBlockedContacts()
                .map {
                    ContactEntityMapper.mapToContactEntityFromDbContactEntity(it)
                }
        } catch (databaseException: DatabaseException) {
            throw DataLayerException(databaseException.message)
        }
    }

    override suspend fun getAllContactsFromDevice(): List<ContactEntity> {
        return contactsProvider.getAllContactsFromDevice()
            .map {
                ContactEntityMapper.mapToContactEntityFromDeviceContactEntity(it)
            }
    }
}