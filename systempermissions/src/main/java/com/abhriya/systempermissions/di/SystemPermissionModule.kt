package com.abhriya.systempermissions.di

import com.abhriya.systempermissions.SystemPermissionsHandler
import com.abhriya.systempermissions.SystemPermissionsHandlerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SystemPermissionModule {

    @Provides
    @Singleton
    fun providesSystemPermissionsHandler(): SystemPermissionsHandler =
        SystemPermissionsHandlerImpl()
}