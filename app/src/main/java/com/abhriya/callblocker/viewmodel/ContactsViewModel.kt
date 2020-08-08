package com.abhriya.callblocker.viewmodel

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhriya.callblocker.domain.ContactsUseCase
import com.abhriya.callblocker.domain.model.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
        println("block called")
        viewModelScope.launch(Dispatchers.IO) {
            _blockedContactsLiveData.postValue(ResourceResult.loading())
            try {
                with(contactsUseCase.getAllBlockedContacts()) {
                    println("block finished")
                    _blockedContactsLiveData.postValue(
                        ResourceResult.success(this)
                    )
                }
            } catch (sqLiteException: SQLiteException) {
                _blockedContactsLiveData.postValue(ResourceResult.error(sqLiteException))
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
                } catch (sqLiteException: SQLiteException) {
                    postValue(ResourceResult.error(sqLiteException))
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
                } catch (sqLiteException: SQLiteException) {
                    postValue(ResourceResult.error(sqLiteException))
                }
            }
        }
    }

    fun getAllSavedAvailableContacts() {
        println("get all contacts")
        viewModelScope.launch(Dispatchers.IO) {
            _savedAvailableContactsLiveData.postValue(ResourceResult.loading())
            try {
                contactsUseCase.getAllSavedAvailableContacts()
                    .also {
                        println("contacts are $it")
                        _savedAvailableContactsLiveData.postValue(ResourceResult.success(it))
                    }
            } catch (sqLiteException: SQLiteException) {
                _savedAvailableContactsLiveData.postValue(ResourceResult.error(sqLiteException))
            }
        }
    }

}