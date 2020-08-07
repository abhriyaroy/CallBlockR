package com.abhriya.database

import android.content.Context
import androidx.room.Room
import com.abhriya.database.blockedcontacts.BlockedContactsDatabase
import com.abhriya.database.blockedcontacts.BlockedContactsDbEntity
import com.abhriya.database.entity.ContactEntity

const val UID_AUTO_INCREMENT = 0

class DatabaseHelper(applicationContext: Context) {

    private val blockedContactsDb: BlockedContactsDatabase = Room.databaseBuilder(
        applicationContext,
        BlockedContactsDatabase::class.java, "blocked-contacts"
    ).build()

    suspend fun saveToBlockedContacts(vararg contactEntities: ContactEntity) {
        contactEntities.map {
            BlockedContactsDbEntity(
                uid = UID_AUTO_INCREMENT,
                name = it.name,
                phoneNumber = it.phoneNumber
            )
        }.forEach {
            blockedContactsDb.contactsDao().insert(it)
        }
    }

    suspend fun removeBlockedContacts(vararg contactsToUnblock: ContactEntity){
        contactsToUnblock.map {
            BlockedContactsDbEntity(
                uid = UID_AUTO_INCREMENT,
                name = it.name,
                phoneNumber = it.phoneNumber
            )
        }.forEach {
            blockedContactsDb.contactsDao().delete(it)
        }
    }
}