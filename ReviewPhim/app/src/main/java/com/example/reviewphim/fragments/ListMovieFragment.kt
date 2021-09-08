package com.example.reviewphim.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.radiofm.services.AppConfig
import com.example.radiofm.services.CallServer
import com.example.reviewphim.MainActivity
import com.example.reviewphim.adapters.MovieAdapter
import com.example.reviewphim.R
import com.example.reviewphim.models.MovieListModel
import com.example.reviewphim.models.ResponseModel
import kotlinx.android.synthetic.main.fragment_list_movie.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ListMovieFragment(private val mainActivity: MainActivity) : Fragment() {

    private var movies:MutableList<MovieListModel> = arrayListOf()
    private val sever: CallServer = AppConfig.retrofit.create(CallServer::class.java)
    private var lastIdApi=0
    private var dyScroll=0
    private var onLoading = false
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var processBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        movieAdapter = MovieAdapter(this@ListMovieFragment)
        val view =  inflater.inflate(R.layout.fragment_list_movie, container, false)
        processBar = view.findViewById(R.id.progressBar_podcast_fragment)
        fetchData(lastIdApi)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_list_movie)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState==0 && dyScroll>1){
                    onLoading = false
                    fetchData(lastIdApi)
                }

                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                dyScroll = dy
            }
        })

        return view
    }

    private fun fetchData(idLast:Int){
        loading(true)
        val listRadio: Call<ResponseModel> = sever.listMovie(idLast)
        listRadio.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){
                    loading(false)

                    val body = response.body()
                    val data = body?.data
                    val list = data?.get("list").toString()

                    val listObject = JSONTokener(list).nextValue() as JSONArray

                    for(i in 0 until listObject.length()){
                        val item = listObject[i].toString()
                        val jsonItem = JSONTokener(item).nextValue() as JSONObject
                        val title = jsonItem.get("title").toString().trim('"')
                        val created = jsonItem.get("created").toString().trim('"')
                        val picture = jsonItem.get("picture_url").toString().trim('"')
                        val video = jsonItem.get("video_url").toString().trim('"')
                        val nameChannel = jsonItem.get("name_channel").toString().trim('"')
                        val countView = jsonItem.get("count_viewer").toString().trim('"').toInt()
                        val countReaction = jsonItem.get("count_reaction").toString().trim('"').toInt()
                        val id = jsonItem.get("id").toString().trim('"').toInt()
                        movies.add(MovieListModel(id,title,created,picture,video,nameChannel,countView,countReaction))
                        lastIdApi = id
                    }
                    //songList = models

                    if(idLast==0){
                        recycler_view_list_movie.apply{
                            val cardView = LinearLayoutManager(context)
                            layoutManager = cardView
                            adapter = movieAdapter
                            setHasFixedSize(true)
                            setItemViewCacheSize(20)
                        }.run {
                            movieAdapter.setData(movies)
                        }
                    }else{
                        movieAdapter.setData(movies)
                    }



                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(activity,"Hệ thống tạm thời gián đoạn, Vui lòng thử lại sau", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun onClickListenerAdapter(movie:MovieListModel){
        movie.count_viewer = movie.count_viewer+1
        movieAdapter.setViewer(movie)
        mainActivity.playMovie(movie)
    }

    fun loading(isLoad:Boolean = false){
        if(isLoad) processBar.visibility = View.VISIBLE
        if(!isLoad) processBar.visibility = View.GONE
    }

    fun setReaction(data:MovieListModel){
        movieAdapter.setViewer(data)
    }


}
