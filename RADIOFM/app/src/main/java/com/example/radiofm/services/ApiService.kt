package com.example.radiofm.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.radiofm.models.ResponseModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.measureTimeMillis

class ApiService {
    private val sever:CallServer = AppConfig.retrofit.create(CallServer::class.java)
    private lateinit var baseRequest:Call<ResponseModel>
    private var responseData:MutableLiveData<ResponseModel>
    init {
        val live = ResponseModel(999,"ERROR SERVIVE", JsonObject())
        responseData = MutableLiveData();
    }

    fun listChannelRadio(): MutableLiveData<ResponseModel> {
        baseRequest = sever.listRadio()

        callApi();
        return responseData;
    }

    private fun callApi() {

        baseRequest.enqueue(object :Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                responseData.postValue( response.body());
                println("===>>$responseData")
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                responseData.postValue( ResponseModel(500,"Error Service", JsonObject()))

            }

        })

    }
}
