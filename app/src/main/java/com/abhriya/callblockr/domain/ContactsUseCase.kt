package com.abhriya.callblockr.domain

import com.abhriya.callblockr.data.ContactsRepository
import com.abhriya.callblockr.domain.mapper.ContactsModelMapper
import com.abhriya.callblockr.domain.model.CallLogModel
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.domain.model.ContactModelType

interface ContactsUseCase {
    suspend fun saveBlockedContact(contactModel: ContactModel)
    suspend fun unBlockContact(contactModel: ContactModel)
    suspend fun getAllBlockedContacts(): List<ContactModel>
    suspend fun getAllSavedAvailableContacts(): List<ContactModel>
    suspend fun getCallLog(): List<CallLogModel>
}

class ContactsInteractor(private val contactsRepository: ContactsRepository) : ContactsUseCase {
    override suspend fun saveBlockedContact(contactModel: ContactModel) {
        contactsRepository.saveBlockedContact(
            ContactsModelMapper.mapToContactEntityFromContactsModel(contactModel)
        )
    }

    override suspend fun unBlockContact(contactModel: ContactModel) {
        contactsRepository.unBlockContact(
            ContactsModelMapper.mapToContactEntityFromContactsModel(contactModel)
        )
    }

    override suspend fun getAllBlockedContacts(): List<ContactModel> {
        return contactsRepository.getAllBlockedContacts()
            .map {
                ContactsModelMapper.mapToContactsModelFromContactEntity(
                    it,
                    ContactModelType.BLOCKED_CONTACT
                )
            }
    }

    override suspend fun getAllSavedAvailableContacts(): List<ContactModel> {
        val savedContacts : List<ContactModel> = contactsRepository.getAllContactsFromDevice()
            .map {
                ContactsModelMapper.mapToContactsModelFromContactEntity(
                    it,
                    ContactModelType.UNBLOCKED_CONTACT
                )
            }
        val blockedContacts = getAllBlockedContacts()
        return savedContacts.filter {
            var isBlocked = true
            for (blockedContact in blockedContacts) {
                if (it.phoneNumber == blockedContact.phoneNumber) {
                    isBlocked = false
                    break
                }
            }
            isBlocked
        }
    }

    override suspend fun getCallLog(): List<CallLogModel> {
        val blockedContacts = getAllBlockedContacts()
        return contactsRepository.getAllCallLogs().map {
            CallLogModel(
                it.contactName,
                it.contactNumber,
                it.callType,
                it.timeStampInMillis,
                it.callDuration,
                blockedContacts.find { contactModel ->
                    contactModel.phoneNumber == it.contactNumber
                } != null
            )
        }
    }


}