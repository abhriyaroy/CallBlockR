package com.abhriya.systempermissions.di

import com.abhriya.systempermissions.SystemPermissionUtil
import com.abhriya.systempermissions.SystemPermissionUtilImpl
import com.abhriya.systempermissions.SystemPermissionsHandler
import com.abhriya.systempermissions.SystemPermissionsHandlerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SystemPermissionModule {

    @Provides
    @Singleton
    fun providesSystemPermissionsHandler(permissionUtil: SystemPermissionUtil): SystemPermissionsHandler =
        SystemPermissionsHandlerImpl(permissionUtil)

    @Provides
    @Singleton
    fun providesSystemPermissionUtil(): SystemPermissionUtil = SystemPermissionUtilImpl()
}