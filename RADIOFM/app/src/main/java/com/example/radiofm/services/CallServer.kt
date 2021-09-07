package com.example.radiofm.services

import com.example.radiofm.models.ResponseModel
import retrofit2.http.*
import retrofit2.Call

interface CallServer {
    @GET("radios/{id}")
    fun detailRadio(
        @Path("id") id:String
    ):Call<ResponseModel>

    @GET("radios")
    fun listRadio():Call<ResponseModel>

    @GET("podcasts")
    fun listPodCat(@Query("song_id") song_id:String):Call<ResponseModel>
}
