package com.abhriya.callblockr.util

fun String.removeAllWhiteSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}