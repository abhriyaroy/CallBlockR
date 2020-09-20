package com.abhriya.systempermissions

interface SystemPermissionUtil {
    fun filterPermissionListForMissingPermissions(permissionsList: List<Pair<String, Boolean>>): List<Pair<String, Boolean>>
}

class SystemPermissionUtilImpl : SystemPermissionUtil {
    override fun filterPermissionListForMissingPermissions(permissionsList: List<Pair<String, Boolean>>): List<Pair<String, Boolean>> {
        return permissionsList.filter {
            !it.second
        }
    }
}