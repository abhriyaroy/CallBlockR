package com.abhriya.callblockr.util

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.use

fun Context.stringRes(@StringRes id: Int) = getString(id)!!

fun Context.drawableRes(@DrawableRes id: Int) =
    ResourcesCompat.getDrawable(resources, id, this.theme)!!

@ColorInt
fun Context.getThemeColor(@AttrRes colorAttrId: Int): Int {
    return obtainStyledAttributes(intArrayOf(colorAttrId))
        .use { it.getColor(0, Color.BLUE) }
}
