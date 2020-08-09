package com.abhriya.contactsprovider

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.abhriya.contactsprovider.model.DeviceContactsEntity
import com.abhriya.systempermissions.SystemPermissionsHandler

interface ContactsProvider {
    suspend fun getAllContactsFromDevice(): List<DeviceContactsEntity>
}

class ContactsProviderImpl(
    private val context: Context,
    private val permissionsHandler: SystemPermissionsHandler
) : ContactsProvider {

    override suspend fun getAllContactsFromDevice(): List<DeviceContactsEntity> {
        if (permissionsHandler.getMissingPermissionListIfAnyOutOfSuppliedPermissionList(
                context, getListOfRequiredPermissions()
            ).isEmpty()
        ) {
            val contactsList = mutableListOf<DeviceContactsEntity>()
            val cr: ContentResolver = context.contentResolver
            val cur: Cursor? = cr.query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null
            )
            if ((cur?.count ?: 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    val id: String = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID)
                    )
                    val name: String? = cur.getString(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )
                    if (cur.getInt(
                            cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                            )
                        ) > 0
                    ) {
                        val pCur: Cursor? = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        while (pCur != null && pCur.moveToNext()) {
                            val phoneNo: String = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            contactsList.add(DeviceContactsEntity(name, phoneNo))
                        }
                        pCur?.close()
                    }
                }
            }
            cur?.close()
            return contactsList
        } else {
            return listOf()
        }
    }

    private fun getListOfRequiredPermissions(): List<String> {
        return mutableListOf(Manifest.permission.READ_CONTACTS)
    }

}