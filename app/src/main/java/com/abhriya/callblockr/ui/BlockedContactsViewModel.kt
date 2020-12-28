//package com.abhriya.callblockr.ui
//
//import androidx.hilt.lifecycle.ViewModelInject
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.abhriya.callblockr.data.exception.DataLayerException
//import com.abhriya.callblockr.domain.ContactsUseCase
//import com.abhriya.callblockr.domain.model.ContactModel
//import com.abhriya.callblockr.util.ResourceState
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.async
//import kotlinx.coroutines.launch
//import java.lang.Exception
//
//class BlockedContactsViewModel
//@ViewModelInject constructor(private val contactsUseCase: ContactsUseCase) : ViewModel() {
//
//    private var _blockedContactsLiveData = MutableLiveData<ResourceState<List<ContactModel>>>()
//    val blockedContactLiveData: LiveData<ResourceState<List<ContactModel>>>
//        get() = _blockedContactsLiveData
//
//    private var _savedAvailableContactsLiveData =
//        MutableLiveData<ResourceState<List<ContactModel>>>()
//
//    fun getAllBlockedContacts() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _blockedContactsLiveData.postValue(ResourceState.loading())
//            try {
//                with(contactsUseCase.getAllBlockedContacts()) {
//                    _blockedContactsLiveData.postValue(
//                        ResourceState.success(this)
//                    )
//                }
//            } catch (dataLayerException: DataLayerException) {
//                _blockedContactsLiveData.postValue(
//                    ResourceState.error(
//                        dataLayerException.message,
//                        dataLayerException
//                    )
//                )
//            }
//        }
//    }
//
//    fun unblockContact(
//        contactModel: ContactModel,
//        updateReceiver: LiveData<ResourceState<List<ContactModel>>>
//    ) {
//        when (updateReceiver) {
//            blockedContactLiveData -> _blockedContactsLiveData
//            else -> _savedAvailableContactsLiveData
//        }.let {
//            try {
//                viewModelScope.launch(Dispatchers.IO) {
//                    it.postValue(ResourceState.loading())
//                    try {
//                        contactsUseCase.unBlockContact(contactModel)
//                        getAllBlockedContacts()
//                        getAllSavedAvailableContacts()
//                    } catch (dataLayerException: DataLayerException) {
//                        it.postValue(
//                            ResourceState.error(dataLayerException.message, dataLayerException)
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    fun blockContact(
//        contactModel: ContactModel,
//        updateReceiver: LiveData<ResourceState<List<ContactModel>>>
//    ) {
//        when (updateReceiver) {
//            blockedContactLiveData -> _blockedContactsLiveData
//            else -> _savedAvailableContactsLiveData
//        }.let {
//            viewModelScope.launch(Dispatchers.IO) {
//                it.postValue(ResourceState.loading())
//                try {
//                    contactsUseCase.saveBlockedContact(contactModel)
//                    val blockedContacts = async { getAllBlockedContacts() }
//                    val availableContacts = async { getAllSavedAvailableContacts() }
//                    availableContacts.await()
//                    blockedContacts.await()
//                } catch (dataLayerException: DataLayerException) {
//                    it.postValue(
//                        ResourceState.error(
//                            dataLayerException.message,
//                            dataLayerException
//                        )
//                    )
//                }
//            }
//        }
//    }
//
//    fun getAllSavedAvailableContacts() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _savedAvailableContactsLiveData.postValue(ResourceState.loading())
//            try {
//                contactsUseCase.getAllSavedAvailableContacts()
//                    .also {
//                        _savedAvailableContactsLiveData.postValue(ResourceState.success(it))
//                    }
//            } catch (dataLayerException: DataLayerException) {
//                _savedAvailableContactsLiveData.postValue(
//                    ResourceState.error(dataLayerException.message, dataLayerException)
//                )
//            }
//        }
//    }
//
//}