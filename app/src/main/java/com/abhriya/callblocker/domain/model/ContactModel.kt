package com.abhriya.callblocker.domain.model

data class ContactModel(
    val name: String? = null,
    val phoneNumber: String,
    val contactModelType: ContactModelType
)

enum class ContactModelType{
    BLOCKED_CONTACT,
    UNBLOCKED_CONTACT
}