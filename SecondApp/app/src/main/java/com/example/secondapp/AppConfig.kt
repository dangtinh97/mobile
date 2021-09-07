package com.example.secondapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppConfig {
    const val BASE_URL = "https://api-dev.vinalife.vn"
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL).
            addConverterFactory(GsonConverterFactory.create())
    val retrofit = builder.build()
}