package com.abhriya.callblockr.domain.model

import com.abhriya.callblockr.data.entity.CallType

data class CallLogModel(
    val contactName: String,
    val contactNumber: String,
    val callType: CallType,
    val timeStampInMillis: String,
    val callDuration: String,
    val isNumberBlocked: Boolean
)