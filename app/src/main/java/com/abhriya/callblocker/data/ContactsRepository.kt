package com.abhriya.callblocker.data

import com.abhriya.callblocker.data.entity.ContactEntity
import com.abhriya.callblocker.data.mapper.ContactEntityMapper
import com.abhriya.database.DatabaseHelper

interface ContactsRepository {
    suspend fun saveBlockedContact(contactEntity: ContactEntity)
    suspend fun unBlockContact(contactEntity: ContactEntity)
    suspend fun getAllBlockedContacts(): List<ContactEntity>
}

class ContactsRepositoryImpl(private val databaseHelper: DatabaseHelper) : ContactsRepository {
    override suspend fun saveBlockedContact(contactEntity: ContactEntity) {
        contactEntity.let {
            ContactEntityMapper.mapToContactEntityFromBlockedContactEntity(it)
        }.also {
            databaseHelper.saveToBlockedContacts(it)
        }
    }

    override suspend fun unBlockContact(contactEntity: ContactEntity) {
        contactEntity.let {
            ContactEntityMapper.mapToContactEntityFromBlockedContactEntity(it)
        }.also {
            databaseHelper.removeBlockedContactsFromDb(it)
        }
    }

    override suspend fun getAllBlockedContacts(): List<ContactEntity> {
        return databaseHelper.getAllBlockedContacts()
            .map {
                ContactEntityMapper.mapToBlockedContactEntityFromContactEntity(it)
            }
    }
}