package com.example.radiofm


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.radiofm.adapters.RadioAdapter
import com.example.radiofm.models.RadioModel
import com.example.radiofm.models.ResponseModel
import com.example.radiofm.services.AppConfig
import com.example.radiofm.services.CallServer
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment (var activity: MainActivity) : Fragment() {

    private val sever:CallServer = AppConfig.retrofit.create(CallServer::class.java)
    lateinit var radioAdapter: RadioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("====onCreate");
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        fetchData();

        return view;
    }

    private fun fetchData(){
        activity.loading(true)
        val listRadio:Call<ResponseModel> = sever.listRadio()
        listRadio.enqueue(object : Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){
                    activity.loading()
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
//                    radioAdapter = RadioAdapter(this@HomeFragment,models)
//                    radio_recycler_view.apply{
//                        val linearLayoutManager = LinearLayoutManager(context)
//                        layoutManager = linearLayoutManager
//                        adapter = radioAdapter
//                        setHasFixedSize(true)
//                        setItemViewCacheSize(20)
//                    }


                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(activity,"Hệ thống tạm thời gián đoạn, Vui lòng thử lại sau",Toast.LENGTH_LONG).show()
            }

        })
    }

    fun fetchDataRadioDetail(id:String){
        activity.loading(true)
        val detailRadio: Call<ResponseModel> = sever.detailRadio(id)

        detailRadio.enqueue(object: Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){
                    activity.loading()
                    println("====ResponseJson"+response.body());
                    val body = response.body()
                    val data = body?.data
                    val link = data?.get("link").toString().trim('"')
                    println("====$link")
                    activity.playRadio(link)
                    radioAdapter.setIdPlayer(id)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(activity,"Hệ thống tạm thời gián đoạn, Vui lòng thử lại sau",Toast.LENGTH_LONG).show()
            }
        })
    }


}
