package com.example.smartplug.models

data class Device (
    val port:String,
    var status:Boolean,
    val name:String = "NO NAME",
    val send_from:String = "no device"
    )
