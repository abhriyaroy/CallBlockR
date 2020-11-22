//package com.abhriya.callblocker.di
//
//import com.abhriya.callblocker.di.scopes.PerActivity
//import com.abhriya.callblocker.ui.MainActivity
//import dagger.Module
//import dagger.android.ContributesAndroidInjector
//
//@Module
//abstract class ActivityBuilder {
//
//    @PerActivity
//    @ContributesAndroidInjector(modules = [(FragmentBuilder::class)])
//    abstract fun mainActivityInjector(): MainActivity
//
//}