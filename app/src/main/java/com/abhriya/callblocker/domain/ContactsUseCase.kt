package com.abhriya.callblocker.domain

import com.abhriya.callblocker.data.ContactsRepository
import com.abhriya.callblocker.domain.mapper.ContactsModelMapper
import com.abhriya.callblocker.domain.model.ContactModel
import com.abhriya.callblocker.domain.model.ContactModelType.BLOCKED_CONTACT

interface ContactsUseCase {
    suspend fun saveBlockedContact(contactModel: ContactModel)
    suspend fun unBlockContact(contactModel: ContactModel)
    suspend fun getAllBlockedContacts(): List<ContactModel>
}

class ContactsInteractor(private val contactsRepository: ContactsRepository) : ContactsUseCase {
    override suspend fun saveBlockedContact(contactModel: ContactModel) {
        contactModel.let {
            ContactsModelMapper.mapToContactEntityFromContactsModel(it)
        }.also {
            contactsRepository.saveBlockedContact(it)
        }
    }

    override suspend fun unBlockContact(contactModel: ContactModel) {
        contactModel.let {
            ContactsModelMapper.mapToContactEntityFromContactsModel(it)
        }.also {
            contactsRepository.unBlockContact(it)
        }
    }

    override suspend fun getAllBlockedContacts(): List<ContactModel> {
        return contactsRepository.getAllBlockedContacts()
            .let {
                it.map { contactEntity ->
                    ContactsModelMapper.mapToContactsModelFromContactEntity(
                        contactEntity,
                        BLOCKED_CONTACT
                    )
                }
            }
    }

}