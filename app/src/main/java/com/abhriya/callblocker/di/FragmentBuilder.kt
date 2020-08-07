package com.abhriya.callblocker.di

import com.abhriya.callblocker.ui.BlockedContactsFragment
import com.abhriya.callblocker.ui.UnBlockedContactsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun providesBlockedContactsFragment(): BlockedContactsFragment

    @ContributesAndroidInjector
    abstract fun providesUnBlockedContactsFragment(): UnBlockedContactsFragment
}