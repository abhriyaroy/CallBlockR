package com.abhriya.callblocker.domain

import com.abhriya.callblocker.data.ContactsRepository
import com.abhriya.callblocker.domain.mapper.ContactsModelMapper
import com.abhriya.callblocker.domain.model.ContactsModel

interface ContactsUseCase {
    suspend fun saveBlockedContact(contactsModel: ContactsModel)
    suspend fun removeBlockedContact(contactsModel: ContactsModel)
    suspend fun getAllBlockedContacts(): List<ContactsModel>
}

class ContactsInteractor(private val contactsRepository: ContactsRepository) : ContactsUseCase {
    override suspend fun saveBlockedContact(contactsModel: ContactsModel) {
        contactsModel.let {
            ContactsModelMapper.mapToContactEntityFromContactsModel(it)
        }.also {
            contactsRepository.saveBlockedContact(it)
        }
    }

    override suspend fun removeBlockedContact(contactsModel: ContactsModel) {
        contactsModel.let {
            ContactsModelMapper.mapToContactEntityFromContactsModel(it)
        }.also {
            contactsRepository.removeBlockedContact(it)
        }
    }

    override suspend fun getAllBlockedContacts(): List<ContactsModel> {
        return contactsRepository.getAllBlockedContacts()
            .let {
                it.map { contactEntity ->
                    ContactsModelMapper.mapToContactsModelFromContactEntity(contactEntity)
                }
            }
    }

}