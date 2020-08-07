package com.abhriya.callblocker.data.mapper

import com.abhriya.callblocker.data.entity.ContactEntity
import com.abhriya.database.entity.ContactDbEntity

internal object ContactEntityMapper {

    internal fun mapToBlockedContactEntityFromContactEntity(dbDbEntity: ContactDbEntity): ContactEntity =
        dbDbEntity.let {
            ContactEntity(it.name, it.phoneNumber)
        }

    internal fun mapToContactEntityFromBlockedContactEntity(contactEntity: ContactEntity): ContactDbEntity =
        contactEntity.let {
            ContactDbEntity(
                name = it.name,
                phoneNumber = it.number
            )
        }
}