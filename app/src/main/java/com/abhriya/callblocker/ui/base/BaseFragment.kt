package com.abhriya.callblocker.ui.base

import androidx.fragment.app.Fragment

abstract class BaseFragment  : Fragment(){
    abstract fun handlePermissionGranted()
}