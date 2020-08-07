package com.abhriya.callblocker.domain.mapper

import com.abhriya.callblocker.data.entity.ContactEntity
import com.abhriya.callblocker.domain.model.ContactModel

internal object ContactsModelMapper {
    internal fun mapToContactsModelFromContactEntity(contactDbEntity: ContactEntity): ContactModel =
        contactDbEntity.let {
            ContactModel(it.name, it.number)
        }

    internal fun mapToContactEntityFromContactsModel(contactModel: ContactModel): ContactEntity =
        contactModel.let {
            ContactEntity(it.name, it.phoneNumber)
        }
}