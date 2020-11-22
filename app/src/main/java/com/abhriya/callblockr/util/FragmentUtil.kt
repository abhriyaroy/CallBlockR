package com.abhriya.callblockr.util

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.stringRes(@StringRes id: Int) = getString(id)!!

fun Fragment.colorRes(@ColorRes id: Int) = resources.getColor(id)