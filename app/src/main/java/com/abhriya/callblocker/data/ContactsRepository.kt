package com.abhriya.callblocker.data

import com.abhriya.callblocker.data.entity.ContactEntity
import com.abhriya.callblocker.data.mapper.ContactEntityMapper
import com.abhriya.contactsprovider.ContactsProvider
import com.abhriya.database.DatabaseHelper

interface ContactsRepository {
    suspend fun saveBlockedContact(contactEntity: ContactEntity)
    suspend fun unBlockContact(contactEntity: ContactEntity)
    suspend fun getAllBlockedContacts(): List<ContactEntity>
    suspend fun getAllContactsFromDevice(): List<ContactEntity>
}

class ContactsRepositoryImpl(
    private val databaseHelper: DatabaseHelper,
    private val contactsProvider: ContactsProvider
) : ContactsRepository {
    override suspend fun saveBlockedContact(contactEntity: ContactEntity) {
        contactEntity.let {
            ContactEntityMapper.mapToDbContactEntityFromContactEntity(it)
        }.also {
            databaseHelper.saveToBlockedContacts(it)
        }
    }

    override suspend fun unBlockContact(contactEntity: ContactEntity) {
        contactEntity.let {
            ContactEntityMapper.mapToDbContactEntityFromContactEntity(it)
        }.also {
            databaseHelper.removeBlockedContactsFromDb(it)
        }
    }

    override suspend fun getAllBlockedContacts(): List<ContactEntity> {
        return databaseHelper.getAllBlockedContacts()
            .map {
                ContactEntityMapper.mapToContactEntityFromDbContactEntity(it)
            }
    }

    override suspend fun getAllContactsFromDevice(): List<ContactEntity> {
        return contactsProvider.getAllContactsFromDevice()
            .map {
                ContactEntityMapper.mapToContactEntityFromDeviceContactEntity(it)
            }
    }
}