package com.example.radiofm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.example.radiofm.AppActivity
import com.example.radiofm.R
import com.example.radiofm.adapters.RadioAdapter
import com.example.radiofm.models.RadioModel
import com.example.radiofm.models.ResponseModel
import com.example.radiofm.services.AppConfig
import com.example.radiofm.services.CallServer
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.radio_recycler_view
import kotlinx.android.synthetic.main.fragment_radio.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RadioFragment(private val appActivity: AppActivity) : Fragment() {

    private val sever: CallServer = AppConfig.retrofit.create(CallServer::class.java)
    lateinit var radioAdapter: RadioAdapter
    lateinit var processBar:ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        println("===RadioFragment")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_radio, container, false)

        processBar = view.findViewById(R.id.progressBar_radio_fragment)
        fetchData();

        val recyclerView = view.findViewById<RecyclerView>(R.id.radio_recycler_view)
        recyclerView.setOnClickListener {

        }

        return view
    }

    private fun fetchData(){
        loading(true)
        val listRadio: Call<ResponseModel> = sever.listRadio()
        listRadio.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){
                    loading(false)
                    println("====ResponseJson"+response.body());
                    val body = response.body()
                    val data = body?.data
                    val list = data?.get("list").toString()
                    val listObject = JSONTokener(list).nextValue() as JSONArray

                    val models = arrayListOf<RadioModel>()
                    for(i in 0..listObject.length()-1){
                        val item = listObject[i].toString()
                        val jsonItem = JSONTokener(item).nextValue() as JSONObject
                        val name = jsonItem.get("name").toString().trim('"')
                        val id = jsonItem.get("id").toString().trim('"')
//                        val moneyText = jsonItem.get("fees_from_text").toString().trim('"')
//                        val typeRedirect = jsonItem.get("type_redirect").toString().trim('"')
//                        val redirectTo = jsonItem.get("redirect_to").toString().trim()
                        models.add(RadioModel("radio",name,id,false))

                    }
                    radioAdapter = RadioAdapter(this@RadioFragment,models)

                    radio_recycler_view.apply{
                        //val linearLayoutManager = LinearLayoutManager(context)
                        val cardView = GridLayoutManager(context,2)
                        layoutManager = cardView
                        adapter = radioAdapter
                        setHasFixedSize(true)
                        setItemViewCacheSize(20)
                    }


                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.e("API_SERVICE",t.toString())
                Toast.makeText(activity,"Hệ thống tạm thời gián đoạn, Vui lòng thử lại sau", Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun loading(isLoad:Boolean = true){

        if(!isLoad) processBar.visibility = View.GONE
        if(isLoad) processBar.visibility = View.VISIBLE
    }

    fun fetchDataRadioDetail(id:String){
        loading(true)
        val detailRadio: Call<ResponseModel> = sever.detailRadio(id)

        detailRadio.enqueue(object: Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){
                    loading(false)
                    println("====ResponseJson"+response.body());
                    val body = response.body()
                    val data = body?.data
                    val link = data?.get("link").toString().trim('"')
                    println("====$link")
                    appActivity.playMusic(link,false)
                    radioAdapter.setIdPlayer(id)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(activity,"Hệ thống tạm thời gián đoạn, Vui lòng thử lại sau",Toast.LENGTH_LONG).show()
            }
        })
    }
}
