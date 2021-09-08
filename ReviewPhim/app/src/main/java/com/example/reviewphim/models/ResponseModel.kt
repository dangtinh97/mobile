package com.example.reviewphim.models

import com.google.gson.JsonObject

data class ResponseModel (
    val status: Int,
    val content:String,
    val data : JsonObject
    )

