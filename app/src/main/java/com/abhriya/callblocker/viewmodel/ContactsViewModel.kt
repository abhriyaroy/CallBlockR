package com.abhriya.callblocker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhriya.callblocker.domain.ContactsUseCase
import com.abhriya.callblocker.domain.model.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(private val contactsUseCase: ContactsUseCase) :
    ViewModel() {

    private var _blockedContactsLiveData = MutableLiveData<ResourceResult<List<ContactModel>>>()
    val blockedContactLiveData: LiveData<ResourceResult<List<ContactModel>>>
        get() = _blockedContactsLiveData

    fun getAllBlockedContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            _blockedContactsLiveData.postValue(ResourceResult.loading())
            _blockedContactsLiveData.postValue(
                ResourceResult.success(contactsUseCase.getAllBlockedContacts())
            )
        }
    }

    fun blockContact(contactModel: ContactModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _blockedContactsLiveData.postValue(ResourceResult.loading())
            contactsUseCase.saveBlockedContact(contactModel)
            _blockedContactsLiveData.postValue(
                ResourceResult.success(contactsUseCase.getAllBlockedContacts())
            )
        }
    }

}