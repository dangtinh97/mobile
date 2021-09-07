package com.example.reviewphim.services

import com.example.radiofm.services.AppConfig
import com.example.radiofm.services.CallServer
import com.example.reviewphim.models.MovieListModel
import com.example.reviewphim.models.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiService {


    private val sever: CallServer = AppConfig.retrofit.create(CallServer::class.java)

    private lateinit var callApi:Call<ResponseModel>

    fun likeMovie(item:MovieListModel){
        callApi= sever.addReaction(item.id)
        callServer()
    }

    fun addViewer(item:MovieListModel){
        callApi= sever.addViewer(item.id)
        callServer()
    }

    public fun installAppFirst(keyApp:String,info:String){
        callApi = sever.installAppFirst(keyApp,info)
        callServer()
    }

    private fun callServer(){
        callApi.enqueue(object : Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {

            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

            }

        })
    }
}
