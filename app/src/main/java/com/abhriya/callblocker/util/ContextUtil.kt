package com.abhriya.callblocker.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.stringRes(@StringRes id: Int) = getString(id)!!

fun Fragment.stringRes(@StringRes id: Int) = getString(id)!!

fun Fragment.colorRes(@ColorRes id: Int) = resources.getColor(id)
