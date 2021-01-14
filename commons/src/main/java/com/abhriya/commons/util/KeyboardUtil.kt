package com.abhriya.commons.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {

    //Use showKeyboard(Context context)
    @Deprecated("")
    fun showKeyBoard(context: Context, view: View?) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager != null && view != null) {
            view!!.setFocusable(true)
            view!!.setFocusableInTouchMode(true)
            inputMethodManager!!.showSoftInput(view, 0)
        }
    }

    fun showKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager != null) {
            inputMethodManager!!.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun hideSoftInputFromWindow(context: Activity) {
        val inputManager = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val currentFocusedView = context.getCurrentFocus()
        if (currentFocusedView != null) {
            inputManager!!.hideSoftInputFromWindow(
                currentFocusedView!!.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

    }

    fun hideKeyboard(view: View?) {
        if (view != null) {
            val inputManager =
                view!!.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputManager != null) {
                inputManager!!.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
            }
        }
    }
}