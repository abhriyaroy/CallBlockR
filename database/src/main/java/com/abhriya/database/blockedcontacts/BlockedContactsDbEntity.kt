package com.abhriya.database.blockedcontacts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
internal data class BlockedContactsDbEntity(
    @ColumnInfo(name = "name") val name: String?,
    @PrimaryKey
    @ColumnInfo(name = "phone_number") val phoneNumber: String
)