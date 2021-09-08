package com.example.radiofm.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.radiofm.AppActivity
import com.example.radiofm.R
import com.example.radiofm.adapters.SongAdapter
import com.example.radiofm.models.ResponseModel
import com.example.radiofm.models.Song
import com.example.radiofm.services.AppConfig
import com.example.radiofm.services.CallServer
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_pod_cast.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class PodCastFragment(private val appActivity: AppActivity) : Fragment() {

    private val sever: CallServer = AppConfig.retrofit.create(CallServer::class.java)
    private lateinit var songAdapter:SongAdapter
    private var songList:MutableList<Song> = arrayListOf()
    private var setList = false;
    private var onLoading = false;
    private var lastIdApi = ""
    private var dyScroll = 0
    private var models : MutableList<Song> = arrayListOf()
    private lateinit var processBar: ProgressBar
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    init {
        songAdapter = SongAdapter(this@PodCastFragment)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        firebaseAnalytics = Firebase.analytics

        val view = inflater.inflate(R.layout.fragment_pod_cast, container, false)
        processBar = view.findViewById(R.id.progressBar_podcast_fragment)
        fetchData("0")
        val recyclerView = view.findViewById<RecyclerView>(R.id.song_recycler_view)

        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState==0 && dyScroll>1){
                    onLoading = false
                    firebaseAnalytics.logEvent("music_load_data") {
                        param("user_"+appActivity.getUserApp(),  Calendar.getInstance().getTime().toString())
                    }
                    //Toast.makeText(context,lastOid,Toast.LENGTH_LONG).show()
                    fetchData(lastIdApi)
                }

                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                dyScroll = dy
            }
        })


        return view;
    }

    private fun fetchData(idLast:String){
        loading(true)
        val listRadio: Call<ResponseModel> = sever.listPodCat(idLast)
        listRadio.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){
                    loading(false)
                    println("====ResponseJson"+response.body());
                    val body = response.body()
                    val data = body?.data
                    val list = data?.get("list").toString()


                    val listObject = JSONTokener(list).nextValue() as JSONArray


                    for(i in 0..listObject.length()-1){
                        val item = listObject[i].toString()
                        val jsonItem = JSONTokener(item).nextValue() as JSONObject
                        val name = jsonItem.get("name").toString().trim('"')
                        val link = jsonItem.get("link").toString().trim('"')
                        val count = jsonItem.get("count").toString().trim('"').toInt()
                        val songId = jsonItem.get("song_id").toString().trim('"').toInt()
                        lastIdApi = jsonItem.get("song_id").toString().trim('"')
                        onLoading = false;
                        val urlImage = jsonItem.get("url").toString().trim('"')
//                        val moneyText = jsonItem.get("fees_from_text").toString().trim('"')
//                        val typeRedirect = jsonItem.get("type_redirect").toString().trim('"')
//                        val redirectTo = jsonItem.get("redirect_to").toString().trim()
//                        appActivity.setMedia(link,false)
                        //appActivity.setMedia(link,false)
                        if(idLast!=="0") appActivity.setMedia(link,false)
                        models.add(Song(name,link,count,songId,urlImage))

                    }
                    songList = models

                    if(idLast=="0"){
                        song_recycler_view.apply{
                            val cardView = LinearLayoutManager(context)
                            layoutManager = cardView
                            adapter = songAdapter
                            setHasFixedSize(true)
                            setItemViewCacheSize(20)
                        }.run {
                            songAdapter.setData(models)
                        }
                    }else{
                        songAdapter.setData(models)
                    }



                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(activity,"Hệ thống tạm thời gián đoạn, Vui lòng thử lại sau", Toast.LENGTH_LONG).show()
            }

        })
    }

     fun playSong(item:Song){
         appActivity.playMusic(item.link,true)
         for (i in 0..songList.size-1){
             appActivity.setMedia(songList[i].link,songList[i].link===item.link)
         }

     }

    fun setUrlIsPlay(url:String){
        songAdapter.setDataIsPlay(url)
    }

    fun loading(isLoad:Boolean = false){
        if(isLoad) processBar.visibility = View.VISIBLE
        if(!isLoad) processBar.visibility = View.GONE
    }


}
