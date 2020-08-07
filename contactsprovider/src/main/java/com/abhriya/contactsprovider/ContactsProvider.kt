package com.abhriya.contactsprovider

import com.abhriya.systempermissions.SystemPermissionsHandler
import javax.inject.Inject

class ContactsProvider(systemPermissionsHandler : SystemPermissionsHandler){

    @Inject
    lateinit var systemPermissionsHandler: SystemPermissionsHandler

}