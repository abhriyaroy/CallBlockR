package com.abhriya.callblocker.di

import android.app.Application
import com.abhriya.callblocker.CallBlockerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import com.abhriya.callblocker.di.scopes.PerApplication

@PerApplication
@Component(
    modules = [(AndroidInjectionModule::class), (AppModule::class)]
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