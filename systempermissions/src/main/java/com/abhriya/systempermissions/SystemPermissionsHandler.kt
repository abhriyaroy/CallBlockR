package com.abhriya.systempermissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object SystemPermissionsHandler {

    fun getMissingPermissionListIfAnyOutOfSuppliedPermissionList(
        context: Context,
        permissionList: List<String>
    ): Pair<Boolean, List<String>> {
        val missingPermissionsList = mutableListOf<String>()
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissionsList.add(permission)
            }
        }
        return missingPermissionsList.isNotEmpty() to missingPermissionsList
    }

    fun requestPermission(activity: Activity, permissionList: List<String>) {
        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionList.toTypedArray(), 0
            )
        }
    }
}