package com.abhriya.commons.di

import com.abhriya.commons.DialogHelper
import com.abhriya.commons.SystemPermissionUtil
import com.abhriya.commons.SystemPermissionUtilImpl
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


//    @Provides
//    @Singleton
//    fun providesSystemPermissionsHandler(permissionUtil: SystemPermissionUtil): SystemPermissionsHandler =
//        SystemPermissionsHandlerImpl(permissionUtil)

    @Provides
    @Singleton
    fun providesSystemPermissionUtil(): SystemPermissionUtil = SystemPermissionUtilImpl()
}