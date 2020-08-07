package com.abhriya.database.blockedcontacts

import androidx.room.*

@Dao
internal interface BlockedContactsDao {
    @Query("SELECT * FROM BlockedContactsDbEntity")
    suspend fun getAll(): List<BlockedContactsDbEntity>

    @Query("SELECT * FROM BlockedContactsDbEntity WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<BlockedContactsDbEntity>

    @Query("SELECT * FROM BlockedContactsDbEntity WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): BlockedContactsDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: BlockedContactsDbEntity)

    @Delete
    suspend fun delete(user: BlockedContactsDbEntity)
}