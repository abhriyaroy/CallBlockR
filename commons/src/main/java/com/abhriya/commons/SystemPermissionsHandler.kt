//package com.abhriya.commons
//
//import android.app.Activity
//import android.content.Context
//import android.content.pm.PackageManager
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//interface SystemPermissionsHandler {
//
//
//
//    fun requestPermission(
//        activity: Activity,
//        permissionList: List<Pair<String, Boolean>>
//    )
//
//}
//
//
//class SystemPermissionsHandlerImpl(private val permissionUtil: SystemPermissionUtil) :
//    SystemPermissionsHandler {
//
//
//
////    override fun requestPermission(
////        activity: Activity,
////        permissionList: List<Pair<String, Boolean>>
////    ) {
////        permissionUtil.filterPermissionListForMissingPermissions(permissionList)
////            .apply {
////                if (this.isNotEmpty()) {
////                    map {
////                        it.first
////                    }.toTypedArray().let {
////                        println(activity.localClassName)
////                        ActivityCompat.requestPermissions(activity, it, PERMISSION_REQUEST_CODE)
////                    }
////                }
////            }
////    }
//}