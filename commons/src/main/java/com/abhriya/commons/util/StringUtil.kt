package com.abhriya.commons.util

fun String.removeAllWhiteSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}