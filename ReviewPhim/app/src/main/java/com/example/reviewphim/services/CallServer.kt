package com.example.radiofm.services

import com.example.reviewphim.models.ResponseModel
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

    @GET("review-phim")
    fun listMovie(@Query("last_id") last_id:Int):Call<ResponseModel>

    @POST("review-phim/{id}/add-reaction")
    fun addReaction(
        @Path("id") id:Int
    ):Call<ResponseModel>

    @POST("review-phim/{id}/add-viewer")
    fun addViewer(
        @Path("id") id:Int
    ):Call<ResponseModel>

    @POST("users/splash-app")
    fun installAppFirst(
        @Query("key_app") key_app:String,
        @Query("data") data:String
    ):Call<ResponseModel>

}
