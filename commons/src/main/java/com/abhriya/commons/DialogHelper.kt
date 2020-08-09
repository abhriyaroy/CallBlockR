package com.abhriya.commons

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input

class DialogHelper {

    fun showInputDialog(
        context: Context,
        titleText: String,
        submitButtonText: String,
        inputType: Int,
        inputValueListener: InputValueListener
    ) {
        MaterialDialog(context).show {
            input(inputType = inputType) { _, text ->
                inputValueListener.onInputSubmitted(text.toString())
            }
            title(text = titleText)
            positiveButton(text = submitButtonText)
        }
    }
}

interface InputValueListener {
    fun onInputSubmitted(inputText: String)
}