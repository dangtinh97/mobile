package com.example.radiofm.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppConfig {
    const val BASE_URL = "http://dangtinh.online/api/"
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL).
        addConverterFactory(GsonConverterFactory.create())
    val retrofit = builder.build()
}
