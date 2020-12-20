package com.abhriya.callblockr.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhriya.callblockr.domain.ContactsUseCase
import com.abhriya.callblockr.domain.model.CallLogModel
import kotlinx.coroutines.launch

class CallLogViewModel @ViewModelInject constructor(private val contactsUseCase: ContactsUseCase) : ViewModel(){

    private val _callLogList : MutableLiveData<List<CallLogModel>> = MutableLiveData()
    val callLogList : MutableLiveData<List<CallLogModel>> = _callLogList

    fun getCallLog(){
        viewModelScope.launch {
            _callLogList.value = contactsUseCase.getCallLog()
        }
    }
}