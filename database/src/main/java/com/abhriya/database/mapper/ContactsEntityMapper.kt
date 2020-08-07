package com.abhriya.database.mapper

import com.abhriya.database.UID_AUTO_INCREMENT
import com.abhriya.database.blockedcontacts.BlockedContactsDbEntity
import com.abhriya.database.entity.ContactEntity

internal object ContactEntityMapper {

    internal fun mapToContactEntityFromBlockedContactsDbEntity(dbEntity: BlockedContactsDbEntity): ContactEntity =
        dbEntity.let {
            ContactEntity(it.name, it.phoneNumber)
        }

    internal fun mapToBlockedContactsDbEntityFromContactEntity(contactEntity: ContactEntity): BlockedContactsDbEntity =
        contactEntity.let {
            BlockedContactsDbEntity(
                uid = UID_AUTO_INCREMENT,
                name = it.name,
                phoneNumber = it.phoneNumber
            )
        }
}