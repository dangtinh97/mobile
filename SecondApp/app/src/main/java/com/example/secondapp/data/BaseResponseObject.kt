package com.example.secondapp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.util.*

data class BaseResponseObject (
    @SerializedName("content") val content: String?,
    @SerializedName("status") val status: Int?,
    @SerializedName("data") val data: JsonObject?,
        ){
    @JvmName("getStatus1")
     fun getStatus(): Int? {
        return this.status
    }

    @JvmName("getContent1")
    fun getContent(): String? {
        return this.content
    }

    @JvmName("getData1")
    fun getData(): JsonObject? {
        return this.data
    }
}