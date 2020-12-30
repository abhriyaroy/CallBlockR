package com.abhriya.callblockr.util

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat

fun Context.stringRes(@StringRes id: Int) = getString(id)!!

fun Context.drawableRes(@DrawableRes id: Int) =
    ResourcesCompat.getDrawable(resources, id, this.theme)!!
