package com.abhriya.callblocker.data

import com.abhriya.callblocker.data.entity.BlockedContactEntity
import com.abhriya.callblocker.data.mapper.ContactEntityMapper
import com.abhriya.database.DatabaseHelper
import com.abhriya.database.entity.ContactEntity

interface ContactsRepository {
    suspend fun saveBlockedContact(blockedContactEntity: BlockedContactEntity)
    suspend fun removeBlockedContact(blockedContactEntity: BlockedContactEntity)
    suspend fun getAllBlockedContacts(): List<BlockedContactEntity>
}

class ContactsRepositoryImpl(private val databaseHelper: DatabaseHelper) : ContactsRepository {
    override suspend fun saveBlockedContact(blockedContactEntity: BlockedContactEntity) {
        blockedContactEntity.let {
            ContactEntityMapper.mapToBlockedContactEntityFromContactEntity(it)
        }.also {
            databaseHelper.saveToBlockedContacts(it)
        }
    }

    override suspend fun removeBlockedContact(blockedContactEntity: BlockedContactEntity) {
        blockedContactEntity.let {
            ContactEntityMapper.mapToBlockedContactEntityFromContactEntity(it)
        }.also {
            databaseHelper.removeBlockedContacts(it)
        }
    }

    override suspend fun getAllBlockedContacts(): List<BlockedContactEntity> {
        return databaseHelper.getAllBlockedContacts()
            .map {
                ContactEntityMapper.mapToBlockedContactEntityFromContactEntity(it)
            }
    }
}