package com.abhriya.callblocker.util

import android.content.Context
import androidx.annotation.StringRes

fun Context.stringRes(@StringRes id: Int) = getString(id)!!