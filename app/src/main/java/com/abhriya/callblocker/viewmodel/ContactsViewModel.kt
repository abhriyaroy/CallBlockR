package com.abhriya.callblocker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhriya.callblocker.data.exception.DataLayerException
import com.abhriya.callblocker.domain.ContactsUseCase
import com.abhriya.callblocker.domain.model.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(private val contactsUseCase: ContactsUseCase) :
    ViewModel() {

    private var _blockedContactsLiveData = MutableLiveData<ResourceResult<List<ContactModel>>>()
    val blockedContactLiveData: LiveData<ResourceResult<List<ContactModel>>>
        get() = _blockedContactsLiveData

    private var _savedAvailableContactsLiveData =
        MutableLiveData<ResourceResult<List<ContactModel>>>()
    val savedAvailableContactLiveData: LiveData<ResourceResult<List<ContactModel>>>
        get() = _savedAvailableContactsLiveData

    fun getAllBlockedContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            _blockedContactsLiveData.postValue(ResourceResult.loading())
            try {
                with(contactsUseCase.getAllBlockedContacts()) {
                    _blockedContactsLiveData.postValue(
                        ResourceResult.success(this)
                    )
                }
            } catch (dataLayerException: DataLayerException) {
                _blockedContactsLiveData.postValue(ResourceResult.error(dataLayerException))
            }
        }
    }

    fun blockContact(
        contactModel: ContactModel,
        updateReceiver: LiveData<ResourceResult<List<ContactModel>>>
    ) {
        when (updateReceiver) {
            blockedContactLiveData -> _blockedContactsLiveData
            else -> _savedAvailableContactsLiveData
        }.apply {
            viewModelScope.launch(Dispatchers.IO) {
                postValue(ResourceResult.loading())
                try {
                    contactsUseCase.saveBlockedContact(contactModel)
                    val blockedContacts = async { getAllBlockedContacts() }
                    val availableContacts = async { getAllSavedAvailableContacts() }
                    availableContacts.await()
                    blockedContacts.await()
                } catch (dataLayerException: DataLayerException) {
                    postValue(ResourceResult.error(dataLayerException))
                }
            }
        }
    }

    fun unblockContact(
        contactModel: ContactModel,
        updateReceiver: LiveData<ResourceResult<List<ContactModel>>>
    ) {
        when (updateReceiver) {
            blockedContactLiveData -> _blockedContactsLiveData
            else -> _savedAvailableContactsLiveData
        }.apply {
            viewModelScope.launch(Dispatchers.IO) {
                postValue(ResourceResult.loading())
                try {
                    contactsUseCase.unBlockContact(contactModel)
                    getAllBlockedContacts()
                    getAllSavedAvailableContacts()
                } catch (dataLayerException: DataLayerException) {
                    postValue(ResourceResult.error(dataLayerException))
                }
            }
        }
    }

    fun getAllSavedAvailableContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            _savedAvailableContactsLiveData.postValue(ResourceResult.loading())
            try {
                contactsUseCase.getAllSavedAvailableContacts()
                    .also {
                        _savedAvailableContactsLiveData.postValue(ResourceResult.success(it))
                    }
            } catch (dataLayerException: DataLayerException) {
                _savedAvailableContactsLiveData.postValue(ResourceResult.error(dataLayerException))
            }
        }
    }

}