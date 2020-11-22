package com.abhriya.callblocker.data.mapper

import com.abhriya.blockedcontacts.data.entity.ContactEntity
import com.abhriya.commons.util.removeAllWhiteSpaces
import com.abhriya.commons.model.DeviceContactsEntity
import com.abhriya.datasource.local.entity.ContactDbEntity

internal object ContactEntityMapper {

    internal fun mapToContactEntityFromDbContactEntity(dbDbEntity: ContactDbEntity): ContactEntity =
        dbDbEntity.let {
            ContactEntity(it.name, it.phoneNumber)
        }

    internal fun mapToDbContactEntityFromContactEntity(contactEntity: ContactEntity): ContactDbEntity =
        contactEntity.let {
            ContactDbEntity(it.name, it.number)
        }

    internal fun mapToDeviceContactEntityFromContactEntity(contactEntity: ContactEntity): DeviceContactsEntity =
        contactEntity.let {
            DeviceContactsEntity(it.name, it.number)
        }

    internal fun mapToContactEntityFromDeviceContactEntity(deviceContactsEntity: DeviceContactsEntity): ContactEntity =
        deviceContactsEntity.let {
            ContactEntity(it.name, it.phoneNumber.removeAllWhiteSpaces())
        }
}