package com.abhriya.systempermissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface SystemPermissionsHandler {

    fun checkPermission(
        context: Context,
        permission: String
    ): Boolean

    fun checkPermissions(
        context: Context,
        permissionList: List<String>
    ): List<Pair<String, Boolean>>

    fun requestPermission(
        activity: Activity,
        permissionList: List<Pair<String, Boolean>>
    )

}

const val PERMISSION_REQUEST_CODE = 101

class SystemPermissionsHandlerImpl(private val permissionUtil: SystemPermissionUtil) :
    SystemPermissionsHandler {

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

    override fun requestPermission(
        activity: Activity,
        permissionList: List<Pair<String, Boolean>>
    ) {
        permissionUtil.filterPermissionListForMissingPermissions(permissionList)
            .apply {
                if (this.isNotEmpty()) {
                    map {
                        it.first
                    }.toTypedArray().let {
                        println(activity.localClassName)
                        ActivityCompat.requestPermissions(activity, it, PERMISSION_REQUEST_CODE)
                    }
                }
            }
    }
}