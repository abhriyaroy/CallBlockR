package com.abhriya.callblockr.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhriya.callblockr.data.entity.CallType
import com.abhriya.callblockr.data.exception.DataLayerException
import com.abhriya.callblockr.domain.ContactsUseCase
import com.abhriya.callblockr.domain.model.CallLogModel
import com.abhriya.callblockr.domain.model.ContactModel
import com.abhriya.callblockr.domain.model.ContactModelType
import com.abhriya.commons.util.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ContactsViewModel @ViewModelInject constructor(private val contactsUseCase: ContactsUseCase) :
    ViewModel() {

    val inputNumberToBlock: MutableLiveData<String> = MutableLiveData()

    private var _blockedContactsLiveData = MutableLiveData<ResourceState<List<ContactModel>>>()
    val blockedContactLiveData: LiveData<ResourceState<List<ContactModel>>>
        get() = _blockedContactsLiveData

    private var _allContactsToShowLiveData =
        MutableLiveData<ResourceState<List<ContactModel>>>()
    val allContactsToShowLiveData: LiveData<ResourceState<List<ContactModel>>>
        get() = _allContactsToShowLiveData

    private val _callLogList: MutableLiveData<List<CallLogModel>> = MutableLiveData()
    val callLogList: MutableLiveData<List<CallLogModel>> = _callLogList

    private var allContactsList = listOf<ContactModel>()

    fun getAllBlockedContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            _blockedContactsLiveData.postValue(ResourceState.loading())
            try {
                with(contactsUseCase.getAllBlockedContacts()) {
                    _blockedContactsLiveData.postValue(
                        ResourceState.success(this)
                    )
                }
            } catch (dataLayerException: DataLayerException) {
                _blockedContactsLiveData.postValue(
                    ResourceState.error(
                        dataLayerException.message,
                        dataLayerException
                    )
                )
            }
        }
    }

    fun blockContact(
        contactModel: ContactModel,
        updateReceiver: LiveData<ResourceState<List<ContactModel>>>
    ) {
        when (updateReceiver) {
            blockedContactLiveData -> _blockedContactsLiveData
            else -> _allContactsToShowLiveData
        }.let {
            viewModelScope.launch(Dispatchers.IO) {
                it.postValue(ResourceState.loading())
                try {
                    contactsUseCase.saveBlockedContact(contactModel)
                    val blockedContacts = async { getAllBlockedContacts() }
                    val availableContacts = async { getAllSavedContacts() }
                    val callLog = async { getCallLog() }
                    availableContacts.await()
                    blockedContacts.await()
                    callLog.await()
                } catch (dataLayerException: DataLayerException) {
                    it.postValue(
                        ResourceState.error(
                            dataLayerException.message,
                            dataLayerException
                        )
                    )
                }
            }
        }
    }

    fun unblockContact(
        contactModel: ContactModel,
        updateReceiver: LiveData<ResourceState<List<ContactModel>>>
    ) {
        when (updateReceiver) {
            blockedContactLiveData -> _blockedContactsLiveData
            else -> _allContactsToShowLiveData
        }.let {
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    it.postValue(ResourceState.loading())
                    try {
                        contactsUseCase.unBlockContact(contactModel)
                        getAllBlockedContacts()
                        getAllSavedContacts()
                    } catch (dataLayerException: DataLayerException) {
                        it.postValue(
                            ResourceState.error(dataLayerException.message, dataLayerException)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllSavedContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            _allContactsToShowLiveData.postValue(ResourceState.loading())
            try {
                contactsUseCase.getAllSavedContacts()
                    .let {
                        val list = it.toMutableList()
                        list.sortWith(Comparator { o1, o2 ->
                            o1.name?.compareTo(o2.name ?: "") ?: 0
                        })
                        list
                    }.also {
                        val here = mutableListOf<ContactModel>(
                            ContactModel("Donal Trump", "+1 889-290-029", ContactModelType.ALL_CONTACT, false),
                            ContactModel("Cristian Ronaldo", "+2848940183", ContactModelType.ALL_CONTACT, false),
                            ContactModel("Joe Bide", "+1 889-290-029", ContactModelType.ALL_CONTACT, false),
                            ContactModel("Justin Trude", "+2848940183", ContactModelType.ALL_CONTACT, false),
                            ContactModel("Lionel Mess", "+1 889-290-029", ContactModelType.ALL_CONTACT, false),
                            ContactModel("Mamata Banner", "+91 11 0202 011", ContactModelType.ALL_CONTACT, false),
                            ContactModel("Narendra Mod", "+91 11 0202 020 ", ContactModelType.ALL_CONTACT, false),
                        )
                        allContactsList = here
                        _allContactsToShowLiveData.postValue(ResourceState.success(here))
                    }
            } catch (dataLayerException: DataLayerException) {
                _allContactsToShowLiveData.postValue(
                    ResourceState.error(dataLayerException.message, dataLayerException)
                )
            }
        }
    }

    fun getCallLog() {
        viewModelScope.launch(Dispatchers.IO) {
            val here = mutableListOf<CallLogModel>(
                CallLogModel("Donal Trump", "+1 889-290-029", CallType.REJECTED_CALL, "1610205717","0", false),
                CallLogModel("Cristian Ronaldo", "+2848940183", CallType.OUTGOING_CALL, "1610205417","160", false),
                CallLogModel("Joe Bide", "+1 889-290-029", CallType.OUTGOING_CALL, "1610203717","10", false),
                CallLogModel("Justin Trude", "+1 889-290-029", CallType.MISSED_CALL, "1610200717","0", false),
                CallLogModel("Lionel Mess", "+1 889-290-029", CallType.INCOMING_CALL, "1610195717","160", false),
                CallLogModel("Mamata Banner", "+91 11 0202 011", CallType.INCOMING_CALL, "1610194717","120", false),
                CallLogModel("Narendra Mod", "+91 11 0202 011", CallType.INCOMING_CALL, "1610193617","132", false),
            )
//            _callLogList.postValue(contactsUseCase.getCallLog())
            _callLogList.postValue(here)
        }
    }

    fun searchAllContacts(stringToSearch: String?) {
        if (stringToSearch?.isNullOrEmpty() != true) {
            viewModelScope.launch(Dispatchers.IO) {
                fuzzySearch(stringToSearch).let {
                    if (it != null) {
                        _allContactsToShowLiveData.postValue(ResourceState.success(it))
                    } else {
                        _allContactsToShowLiveData.postValue(ResourceState.success(listOf()))
                    }
                }
            }
        } else {
            _allContactsToShowLiveData.postValue(ResourceState.success(allContactsList))
        }
    }

    fun closeSearch() {
        _allContactsToShowLiveData.value = ResourceState.success(allContactsList)
    }


    private fun fuzzySearch(str: String): List<ContactModel>? {
        val filterList: MutableList<ContactModel> =
            ArrayList<ContactModel>() // 过滤后的list
        //if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
        if (str.matches(Regex("^([0-9]|[/+]).*"))) { // 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            val simpleStr = str.replace("\\-|\\s".toRegex(), "")
            for (contact in allContactsList) {
                if (contact.phoneNumber != null && contact.name != null) {
                    if (contact.phoneNumber.contains(simpleStr) || contact.name.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact)
                        }
                    }
                }
            }
        } else {
            for (contact in allContactsList) {
                if (contact.phoneNumber != null && contact.name != null) {
                    val isNameContains: Boolean =
                        contact.name.toLowerCase().contains(str.toLowerCase())
                    val isSortKeyContains: Boolean =
                        contact.name.toLowerCase().replace(" ", "")
                            .contains(str.toLowerCase())
                    val isSimpleSpellContains: Boolean =
                        contact.name.toLowerCase().contains(str.toLowerCase())
                    val isWholeSpellContains: Boolean =
                        contact.name.toLowerCase().contains(str.toLowerCase())
                    if (isNameContains || isSortKeyContains || isSimpleSpellContains || isWholeSpellContains) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact)
                        }
                    }
                }
            }
        }
        return filterList
    }

}