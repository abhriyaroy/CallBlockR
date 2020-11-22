package com.abhriya.datasource.mapper

import com.abhriya.datasource.local.blockedcontact.BlockedContactsDbEntity
import com.abhriya.datasource.local.entity.ContactDbEntity

internal object ContactEntityMapper {

    internal fun mapToContactEntityFromBlockedContactsDbEntity(dbEntity: BlockedContactsDbEntity): ContactDbEntity =
        dbEntity.let {
            ContactDbEntity(
                it.name,
                it.phoneNumber
            )
        }

    internal fun mapToBlockedContactsDbEntityFromContactEntity(contactDbEntity: ContactDbEntity): BlockedContactsDbEntity =
        contactDbEntity.let {
            BlockedContactsDbEntity(
                name = it.name,
                phoneNumber = it.phoneNumber
            )
        }
}