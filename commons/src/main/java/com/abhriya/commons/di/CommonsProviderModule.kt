package com.abhriya.commons.di

import android.content.Context
import com.abhriya.commons.DialogHelper
import com.abhriya.systempermissions.SystemPermissionUtil
import com.abhriya.systempermissions.SystemPermissionUtilImpl
import com.abhriya.systempermissions.SystemPermissionsHandler
import com.abhriya.systempermissions.SystemPermissionsHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class CommonsProviderModule {

    @Singleton
    @Provides
    fun providesDialogHelper(): DialogHelper = DialogHelper()


    @Provides
    @Singleton
    fun providesSystemPermissionsHandler(permissionUtil: SystemPermissionUtil): SystemPermissionsHandler =
        SystemPermissionsHandlerImpl(permissionUtil)

    @Provides
    @Singleton
    fun providesSystemPermissionUtil(): SystemPermissionUtil = SystemPermissionUtilImpl()
}