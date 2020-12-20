package com.abhriya.callblockr.data.entity

data class CallLogEntity(
    val contactName: String,
    val contactNumber: String,
    val callType: CallType,
    val timeStampInMillis: String,
    val callDuration: String
)

enum class CallType(type: Int) {
    INCOMING_CALL(1),
    OUTGOING_CALL(2),
    MISSED_CALL(3),
    VOICE_MAIL(4),
    REJECTED_CALL(5),
    BLOCKED_CALL(6),
    UNKNOWN(7);
}