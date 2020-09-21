package com.abhriya.callblocker.di

import android.app.Application
import com.abhriya.callblocker.CallBlockerApplication
import com.abhriya.commons.di.CommonsProviderModule
import com.abhriya.contactsprovider.di.ContactsProviderModule
import com.abhriya.blockedcontactsdatabase.di.DatabaseProviderModule
import com.abhriya.notificationsprovider.di.NotificationsProviderModule
import com.abhriya.phonereceiver.di.PhoneReceiverModule
import com.abhriya.systempermissions.di.SystemPermissionModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AndroidInjectionModule::class), (ActivityBuilder::class), (AppModule::class),
        (ContactsProviderModule::class), (CommonsProviderModule::class), (SystemPermissionModule::class),
        (DatabaseProviderModule::class), (DomainModule::class), (ViewModelFactoryModule::class),
        (BroadcastReceiverModule::class), (PhoneReceiverModule::class), (NotificationsProviderModule::class),
        (ServiceBuilder::class)]
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