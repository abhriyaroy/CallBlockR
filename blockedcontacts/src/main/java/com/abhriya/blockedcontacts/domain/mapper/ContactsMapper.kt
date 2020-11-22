package com.abhriya.callblocker.domain.mapper

import com.abhriya.blockedcontacts.data.entity.ContactEntity
import com.abhriya.blockedcontacts.model.ContactModel
import com.abhriya.blockedcontacts.model.ContactModelType

internal object ContactsModelMapper {
    internal fun mapToContactsModelFromContactEntity(
        contactDbEntity: ContactEntity,
        contactModelType: ContactModelType
    ): ContactModel =
        contactDbEntity.let {
            ContactModel(it.name, it.number, contactModelType)
        }

    internal fun mapToContactEntityFromContactsModel(contactModel: ContactModel): ContactEntity =
        contactModel.let {
            ContactEntity(it.name, it.phoneNumber)
        }
}