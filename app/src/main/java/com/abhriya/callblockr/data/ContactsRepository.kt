package com.abhriya.callblockr.data

import com.abhriya.callblockr.calllogprovider.CallLogProvider
import com.abhriya.callblockr.contactsprovider.ContactsProvider
import com.abhriya.callblockr.data.entity.CallLogEntity
import com.abhriya.callblockr.data.entity.ContactEntity
import com.abhriya.callblockr.data.exception.DataLayerException
import com.abhriya.callblockr.data.mapper.ContactEntityMapper
import com.abhriya.callblockr.domain.model.CallLogModel
import com.abhriya.datasource.local.LocalDataSource
import com.abhriya.datasource.local.exception.DatabaseException

interface ContactsRepository {
    suspend fun saveBlockedContact(contactEntity: ContactEntity)
    suspend fun unBlockContact(contactEntity: ContactEntity)
    suspend fun getAllBlockedContacts(): List<ContactEntity>
    suspend fun getAllContactsFromDevice(): List<ContactEntity>
    suspend fun getAllCallLogs(): List<CallLogEntity>
}

class ContactsRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val contactsProvider: ContactsProvider,
    private val callLogProvider: CallLogProvider,
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

    override suspend fun getAllCallLogs(): List<CallLogEntity> {
        return callLogProvider.getCallLog()
    }
}