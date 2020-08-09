package com.abhriya.commons.util

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

fun Context.stringRes(@StringRes id: Int) = getString(id)!!

fun Context.drawableRes(@DrawableRes id: Int) = resources.getDrawable(id)!!
