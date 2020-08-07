package com.abhriya.database

import android.content.Context
import androidx.room.Room
import com.abhriya.database.blockedcontacts.BlockedContactsDatabase
import com.abhriya.database.blockedcontacts.BlockedContactsDbEntity
import com.abhriya.database.entity.ContactEntity
import com.abhriya.database.mapper.ContactEntityMapper

const val UID_AUTO_INCREMENT = 0

class DatabaseHelper(applicationContext: Context) {

    private val blockedContactsDb: BlockedContactsDatabase = Room.databaseBuilder(
        applicationContext,
        BlockedContactsDatabase::class.java, "blocked-contacts"
    ).build()

    suspend fun saveToBlockedContacts(vararg contactToBlock: ContactEntity) {
        contactToBlock.map {
            ContactEntityMapper.mapToBlockedContactsDbEntityFromContactEntity(it)
        }.forEach {
            blockedContactsDb.contactsDao().insert(it)
        }
    }

    suspend fun removeBlockedContacts(vararg contactToUnblock: ContactEntity) {
        contactToUnblock.map {
            ContactEntityMapper.mapToBlockedContactsDbEntityFromContactEntity(it)
        }.forEach {
            blockedContactsDb.contactsDao().delete(it)
        }
    }

    suspend fun getAllBlockedContacts(): List<ContactEntity> {
        return blockedContactsDb.contactsDao().getAll()
            .map {
                ContactEntityMapper.mapToContactEntityFromBlockedContactsDbEntity(it)
            }
    }
}