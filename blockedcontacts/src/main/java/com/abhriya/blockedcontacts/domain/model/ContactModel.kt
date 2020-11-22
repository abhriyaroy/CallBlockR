package com.abhriya.blockedcontacts.domain.model


data class ContactModel(
    val name: String? = null,
    val phoneNumber: String,
    val contactModelType: ContactModelType
) : Comparable<ContactModel> {
    override fun compareTo(other: ContactModel): Int {
        return if (name == other.name && phoneNumber == other.phoneNumber && contactModelType == other.contactModelType) {
            0
        } else {
            1
        }
    }
}

enum class ContactModelType {
    BLOCKED_CONTACT,
    UNBLOCKED_CONTACT
}