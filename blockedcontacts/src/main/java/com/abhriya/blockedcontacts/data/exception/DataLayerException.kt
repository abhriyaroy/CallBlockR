package com.abhriya.callblocker.data.exception

class DataLayerException(message: String? = null) :
    Exception(message ?: "Something went wrong while interacting with the datasource")