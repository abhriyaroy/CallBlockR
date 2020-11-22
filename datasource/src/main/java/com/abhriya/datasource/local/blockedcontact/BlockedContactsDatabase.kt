package com.abhriya.datasource.local.blockedcontact

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BlockedContactsDbEntity::class], version = 1)
internal abstract class BlockedContactsDatabase : RoomDatabase() {
    abstract fun contactsDao(): BlockedContactsDao
}