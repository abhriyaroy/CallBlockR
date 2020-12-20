package com.abhriya.commons.util

import android.app.Activity
import androidx.annotation.StringRes

fun Activity.stringRes(@StringRes id: Int) = getString(id)!!