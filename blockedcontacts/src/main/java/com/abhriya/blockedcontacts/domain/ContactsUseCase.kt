package com.abhriya.callblocker.domain

import com.abhriya.callblocker.data.ContactsRepository
import com.abhriya.callblocker.domain.mapper.ContactsModelMapper
import com.abhriya.blockedcontacts.model.ContactModel
import com.abhriya.blockedcontacts.model.ContactModelType.BLOCKED_CONTACT
import com.abhriya.blockedcontacts.model.ContactModelType.UNBLOCKED_CONTACT

interface ContactsUseCase {
    suspend fun saveBlockedContact(contactModel: ContactModel)
    suspend fun unBlockContact(contactModel: ContactModel)
    suspend fun getAllBlockedContacts(): List<ContactModel>
    suspend fun getAllSavedAvailableContacts(): List<ContactModel>
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
            .map {
                ContactsModelMapper.mapToContactsModelFromContactEntity(it, BLOCKED_CONTACT)
            }
    }

    override suspend fun getAllSavedAvailableContacts(): List<ContactModel> {
        val savedContacts = contactsRepository.getAllContactsFromDevice()
            .map {
                ContactsModelMapper.mapToContactsModelFromContactEntity(it, UNBLOCKED_CONTACT)
            }
        val blockedContacts = getAllBlockedContacts()
        return savedContacts.filter { availableContact ->
            var isBlocked = true
            for (blockedContact in blockedContacts) {
                if (availableContact.phoneNumber == blockedContact.phoneNumber) {
                    isBlocked = false
                    break
                }
            }
            isBlocked
        }
    }


}