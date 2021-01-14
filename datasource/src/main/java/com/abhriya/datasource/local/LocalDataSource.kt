package com.abhriya.datasource.local

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Room
import com.abhriya.datasource.local.blockedcontact.BlockedContactsDatabase
import com.abhriya.datasource.local.entity.ContactDbEntity
import com.abhriya.datasource.local.exception.DatabaseException
import com.abhriya.datasource.mapper.ContactEntityMapper

interface LocalDataSource {
    suspend fun saveToBlockedContactsDb(contactListToBlock: List<ContactDbEntity>)
    suspend fun removeBlockedContactsFromDb(contactListToUnBlock: List<ContactDbEntity>)
    suspend fun getAllBlockedContacts(): List<ContactDbEntity>
    suspend fun getBlockedContactByNumber(phoneNumber: String?): ContactDbEntity?
}

class LocalDataSourceImpl(applicationContext: Context) :
    LocalDataSource {

    private val blockedContactsDb: BlockedContactsDatabase = Room.databaseBuilder(
        applicationContext,
        BlockedContactsDatabase::class.java, "blocked-contacts"
    ).build()

    override suspend fun saveToBlockedContactsDb(contactListToBlock: List<ContactDbEntity>) {
        try {
            contactListToBlock.map {
                ContactEntityMapper.mapToBlockedContactsDbEntityFromContactEntity(it)
            }.forEach {
                blockedContactsDb.contactsDao().insert(it)
            }
        } catch (sqLiteException: SQLiteException) {
            throw DatabaseException()
        }
    }

    override suspend fun removeBlockedContactsFromDb(contactListToUnBlock: List<ContactDbEntity>) {
        try {
            contactListToUnBlock.map {
                ContactEntityMapper.mapToBlockedContactsDbEntityFromContactEntity(it)
            }.forEach {
                blockedContactsDb.contactsDao().delete(it.phoneNumber)
            }
        } catch (sqLiteException: SQLiteException) {
            throw DatabaseException()
        }
    }

    override suspend fun getAllBlockedContacts(): List<ContactDbEntity> {
        try {
            return blockedContactsDb.contactsDao().getAll()
                .map {
                    ContactEntityMapper.mapToContactEntityFromBlockedContactsDbEntity(it)
                }
        } catch (sqLiteException: SQLiteException) {
            throw DatabaseException()
        }
    }

    override suspend fun getBlockedContactByNumber(phoneNumber: String?): ContactDbEntity? {
        if(phoneNumber.isNullOrEmpty()){
            return null
        }
        try {
            return blockedContactsDb.contactsDao().getAll()
                .find {
                    phoneNumber.contains(it.phoneNumber)
                }?.let {
                    ContactEntityMapper.mapToContactEntityFromBlockedContactsDbEntity(it)
                }
        } catch (sqLiteException: SQLiteException) {
            throw DatabaseException()
        }
    }
}