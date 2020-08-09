package com.abhriya.database.blockedcontact

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class BlockedContactsDbEntity(
    @ColumnInfo(name = "name") val name: String?,
    @PrimaryKey
    @ColumnInfo(name = "phone_number") val phoneNumber: String
)