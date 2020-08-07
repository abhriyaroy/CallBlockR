package com.abhriya.callblocker.data.mapper

import com.abhriya.callblocker.data.entity.BlockedContactEntity
import com.abhriya.database.UID_AUTO_INCREMENT
import com.abhriya.database.entity.ContactEntity

internal object ContactEntityMapper {

    internal fun mapToBlockedContactEntityFromContactEntity(dbEntity: ContactEntity): BlockedContactEntity =
        dbEntity.let {
            BlockedContactEntity(it.name, it.phoneNumber)
        }

    internal fun mapToBlockedContactEntityFromContactEntity(blockedContactEntity: BlockedContactEntity): ContactEntity =
        blockedContactEntity.let {
            ContactEntity(
                name = it.name,
                phoneNumber = it.number
            )
        }
}