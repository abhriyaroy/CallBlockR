package com.abhriya.systempermissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.abhriya.commons.util.stringRes
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

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

class SystemPermissionsHandlerImpl : SystemPermissionsHandler {

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
//        permissionsCallback: PermissionsCallback
    ) {
        with(Random.nextInt(0, 100)) {
            if (permissionList.isNotEmpty()) {
//                [this] = permissionList to permissionsCallback
                ActivityCompat.requestPermissions(
                    activity,
                    permissionList.filter {
                        println(it)
                        !it.second }
                        .map {
                            it.first
                        }.toTypedArray(), this
                )
            }
        }
    }
}