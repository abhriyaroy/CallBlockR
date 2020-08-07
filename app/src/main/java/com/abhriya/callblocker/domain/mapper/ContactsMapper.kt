package com.abhriya.callblocker.domain.mapper

import com.abhriya.callblocker.data.entity.ContactEntity
import com.abhriya.callblocker.domain.model.ContactsModel
import com.abhriya.database.entity.ContactDbEntity

internal object ContactsModelMapper {
    internal fun mapToContactsModelFromContactEntity(contactDbEntity: ContactEntity): ContactsModel =
        contactDbEntity.let {
            ContactsModel(it.name, it.number)
        }

    internal fun mapToContactEntityFromContactsModel(contactsModel: ContactsModel): ContactEntity =
        contactsModel.let {
            ContactEntity(it.name, it.phoneNumber)
        }
}