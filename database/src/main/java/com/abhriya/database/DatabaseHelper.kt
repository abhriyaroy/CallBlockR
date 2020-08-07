package com.abhriya.database

import android.content.Context
import androidx.room.Room
import com.abhriya.database.blockedcontacts.BlockedContactsDatabase
import com.abhriya.database.entity.ContactDbEntity
import com.abhriya.database.mapper.ContactEntityMapper

class DatabaseHelper(applicationContext: Context) {

    private val blockedContactsDb: BlockedContactsDatabase = Room.databaseBuilder(
        applicationContext,
        BlockedContactsDatabase::class.java, "blocked-contacts"
    ).build()

    suspend fun saveToBlockedContacts(vararg contactDbToBlock: ContactDbEntity) {
        contactDbToBlock.map {
            ContactEntityMapper.mapToBlockedContactsDbEntityFromContactEntity(it)
        }.forEach {
            blockedContactsDb.contactsDao().insert(it)
        }
    }

    suspend fun removeBlockedContactsFromDb(vararg contactDbToUnblock: ContactDbEntity) {
        contactDbToUnblock.map {
            ContactEntityMapper.mapToBlockedContactsDbEntityFromContactEntity(it)
        }.forEach {
            blockedContactsDb.contactsDao().delete(it.phoneNumber)
        }
    }

    suspend fun getAllBlockedContacts(): List<ContactDbEntity> {
        return blockedContactsDb.contactsDao().getAll()
            .map {
                ContactEntityMapper.mapToContactEntityFromBlockedContactsDbEntity(it)
            }
    }

    suspend fun getBlockedContactByNumber(phoneNumber: String): ContactDbEntity? {
        return blockedContactsDb.contactsDao().findByNumber(phoneNumber)
            ?.let {
                ContactEntityMapper.mapToContactEntityFromBlockedContactsDbEntity(it)
            }
    }
}