package com.abhriya.callblocker.di

import android.app.Application
import com.abhriya.callblocker.CallBlockerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import com.abhriya.commons.di.CommonsProviderModule
import com.abhriya.contactsprovider.di.ContactsProviderModule
import com.abhriya.database.di.DatabaseProviderModule
import com.abhriya.systempermissions.di.SystemPermissionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AndroidInjectionModule::class), (ActivityBuilder::class), (AppModule::class),
        (ContactsProviderModule::class), (CommonsProviderModule::class), (SystemPermissionModule::class),
        (DatabaseProviderModule::class), (DomainModule::class)]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: CallBlockerApplication)

}