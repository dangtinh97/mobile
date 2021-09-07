package com.example.radiofm.models

import com.google.gson.JsonObject
import java.util.*

data class ResponseModel (
    val status: Int,
    val content:String,
    val data : JsonObject
    )

