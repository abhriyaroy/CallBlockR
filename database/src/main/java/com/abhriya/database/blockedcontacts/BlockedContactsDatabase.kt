package com.abhriya.database.blockedcontacts

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BlockedContactsDbEntity::class], version = 1)
abstract class BlockedContactsDatabase : RoomDatabase() {
    abstract fun contactsDao(): BlockedContactsDao
}