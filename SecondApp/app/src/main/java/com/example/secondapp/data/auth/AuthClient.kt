package com.example.secondapp.data.auth

import com.example.secondapp.data.BaseResponseObject
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface AuthClient {
    @POST("/login")
    fun login(
        @Query("mobile") mobile:String,
        @Query("password") password:String,
    ): Call<BaseResponseObject>

    @GET("/account")
    fun account(@Header("Authorization") authorization:String): Call<BaseResponseObject>

    @GET("/setup-app")
    fun setupApp() : Call<BaseResponseObject>;

    @GET("/home")
    fun home(@Header("Authorization") authorization:String): Call<BaseResponseObject>

    @GET("/user/notification")
    fun notification(@Header("Authorization") authorization:String,
    @Query("last_oid") last_oid:String
    ):Call<BaseResponseObject>

    @GET("/get-bank")
    fun listBank():Call<BaseResponseObject>
}