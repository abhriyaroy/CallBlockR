package com.abhriya.blockedcontactsdatabase.blockedcontact

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface BlockedContactsDao {
    @Query("SELECT * FROM BlockedContactsDbEntity")
    suspend fun getAll(): List<BlockedContactsDbEntity>

    @Query("SELECT * FROM BlockedContactsDbEntity WHERE phone_number LIKE '%' || :number || '%' LIMIT 1")
    suspend fun findByNumber(number: String): BlockedContactsDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: BlockedContactsDbEntity)

    @Query("DELETE from BlockedContactsDbEntity WHERE phone_number LIKE :phoneNumber")
    suspend fun delete(phoneNumber: String)
}