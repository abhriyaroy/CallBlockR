package com.abhriya.commons

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

interface SystemPermissionUtil {
    fun checkPermission(
        context: Context,
        permission: String
    ): Boolean

    fun checkPermissions(
        context: Context,
        permissionList: List<String>
    ): List<Pair<String, Boolean>>

    fun getMissingPermissionsArray(allPermissionsList: List<Pair<String, Boolean>>): Array<String>

}

class SystemPermissionUtilImpl : SystemPermissionUtil {

    override fun checkPermission(context: Context, permission: String): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun checkPermissions(
        context: Context,
        permissionList: List<String>
    ): List<Pair<String, Boolean>> {
        return mutableListOf<Pair<String, Boolean>>()
            .apply {
                for (permission in permissionList) {
                    add(permission to checkPermission(context, permission))
                }
            }
    }

    override fun getMissingPermissionsArray(allPermissionsList: List<Pair<String, Boolean>>): Array<String> {
        return allPermissionsList.filter {
            !it.second
        }.run {
            this.filter { !it.second }.map { it.first }
                .let {
                    if (it.isNullOrEmpty()) {
                        listOf()
                    } else {
                        it
                    }
                }.toTypedArray()
        }
    }

}