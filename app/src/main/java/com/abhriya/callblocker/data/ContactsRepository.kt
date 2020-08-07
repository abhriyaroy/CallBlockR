package com.abhriya.callblocker.data

import com.abhriya.callblocker.data.entity.BlockedContactEntity

interface ContactsRepository {
    fun saveBlockedContact(blockedContactEntity: BlockedContactEntity)
}

class ContactsRepositoryImpl() : ContactsRepository{
    override fun saveBlockedContact(blockedContactEntity: BlockedContactEntity) {

    }
}