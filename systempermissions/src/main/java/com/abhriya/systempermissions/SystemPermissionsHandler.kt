package com.abhriya.systempermissions

import android.Manifest
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
import androidx.core.content.PermissionChecker
import com.abhriya.commons.util.stringRes
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random


interface SystemPermissionsHandler {
    fun getMissingPermissionListIfAnyOutOfSuppliedPermissionList(
        context: Context,
        permissionList: List<String>
    ): Pair<Boolean, List<String>>

    fun requestPermission(
        activity: Activity,
        permissionList: List<String>,
        permissionsCallback: PermissionsCallback
    )

    fun onPermissionResult(
        activity: Activity,
        coordinatorLayout: CoordinatorLayout,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}

class SystemPermissionsHandlerImpl : SystemPermissionsHandler {

    private val requestsMap = hashMapOf<Int, Pair<List<String>, PermissionsCallback>>()

    override fun getMissingPermissionListIfAnyOutOfSuppliedPermissionList(
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

    override fun requestPermission(
        activity: Activity,
        permissionList: List<String>,
        permissionsCallback: PermissionsCallback
    ) {
        with(Random.nextInt(0, 100)) {
            if (permissionList.isNotEmpty()) {
                requestsMap[this] = permissionList to permissionsCallback
                ActivityCompat.requestPermissions(
                    activity,
                    permissionList.toTypedArray(), this
                )
            }
        }
    }

    override fun onPermissionResult(
        activity: Activity,
        coordinatorLayout: CoordinatorLayout,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        for (i in permissions.indices) {
            val permission = permissions[i]
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                val showRationale = shouldShowRequestPermissionRationale(activity, permission)
                if (!showRationale) {
                    showOpenSettingsSnackBar(activity, coordinatorLayout)
                } else {
                    showGrantPermissionSnackBar(
                        activity,
                        coordinatorLayout,
                        listOf(permission),
                        requestCode
                    )
                }
            }
        }
    }

    private fun showOpenSettingsSnackBar(
        activity: Activity,
        coordinatorLayout: CoordinatorLayout
    ) {
        Snackbar.make(
            coordinatorLayout,
            activity.stringRes(R.string.accept_permission_from_settings),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            activity.stringRes(R.string.open_settings)
        ) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.packageName)
            )
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }.show()
    }

    private fun showGrantPermissionSnackBar(
        activity: Activity,
        coordinatorLayout: CoordinatorLayout,
        permissionList: List<String>,
        requestCode: Int
    ) {
        Snackbar.make(
            coordinatorLayout,
            activity.stringRes(R.string.accept_permission),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            activity.stringRes(R.string.grant_permission)
        ) {
            requestPermission(activity, permissionList, requestsMap[requestCode]!!.second)
        }.show()
    }
}