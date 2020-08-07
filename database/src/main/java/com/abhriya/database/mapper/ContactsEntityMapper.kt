package com.abhriya.database.mapper

import com.abhriya.database.blockedcontacts.BlockedContactsDbEntity
import com.abhriya.database.entity.ContactDbEntity

internal object ContactEntityMapper {

    internal fun mapToContactEntityFromBlockedContactsDbEntity(dbEntity: BlockedContactsDbEntity): ContactDbEntity =
        dbEntity.let {
            ContactDbEntity(it.name, it.phoneNumber)
        }

    internal fun mapToBlockedContactsDbEntityFromContactEntity(contactDbEntity: ContactDbEntity): BlockedContactsDbEntity =
        contactDbEntity.let {
            BlockedContactsDbEntity(
                name = it.name,
                phoneNumber = it.phoneNumber
            )
        }
}