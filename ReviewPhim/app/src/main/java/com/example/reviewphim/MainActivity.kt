package com.example.reviewphim

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings

import android.view.MotionEvent
import android.view.View

import android.webkit.MimeTypeMap

import android.widget.RelativeLayout

import android.widget.Toast
import com.example.reviewphim.adapters.AppViewPagerAdapter
import com.example.reviewphim.fragments.ListMovieFragment
import com.example.reviewphim.models.MovieListModel
import com.example.reviewphim.services.ApiService

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.exo_player_control_view.*
import java.util.*
import kotlin.system.exitProcess


const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"

const val HLS_STATIC_URL = "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
const val STATE_RESUME_WINDOW = "resumeWindow"
const val STATE_RESUME_POSITION = "resumePosition"
const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
const val STATE_PLAYER_PLAYING = "playerOnPlay"

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var exoPlayer:SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerView: PlayerView
    private var pressedTime: Long = 0
    private lateinit var movie:MovieListModel
    private var inScreenPlay = false
    private var yOrigin = (0).toFloat()
    private var moveDown = 0;

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private var mediaItem = MediaItem.Builder()
        .setUri(HLS_STATIC_URL)
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .build()

    private val apiService=ApiService()

    private lateinit var listMovieFragment: ListMovieFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerView = findViewById(R.id.player_view)
        playerView.visibility = View.GONE
        title_movie_relative_layout.visibility = View.GONE
        if(!isOnline(this)){
            no_internet.visibility=View.VISIBLE
            return;
        }

        firebaseAnalytics = Firebase.analytics

        listenerActionMovie()


        listMovieFragment = ListMovieFragment(this)
        val adapter = AppViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(listMovieFragment,"Trang chủ")
        main_view_pager.adapter = adapter
        main_view_pager.setCurrentItem(0)



        //exoplayer
        exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
        }


        playerView.setOnTouchListener { v, event ->
            val pCount = event.pointerCount
            var actString=""
            var  y:Float = (0).toFloat();
            for (i in 0 until pCount){
                 y = event.getY(i)
                val act = event.action

                when(act){
                    MotionEvent.ACTION_POINTER_DOWN->actString = "PTR DOWN"
                    MotionEvent.ACTION_POINTER_UP->actString = "ACTION_POINTER_UP"
                    MotionEvent.ACTION_DOWN->actString = "ACTION_DOWN"
                    MotionEvent.ACTION_UP->actString = "ACTION_UP"
                    MotionEvent.ACTION_MOVE->actString = "ACTION_MOVE"
                }
            }

            if(actString==="ACTION_MOVE" && y>yOrigin){
                yOrigin = y
                moveDown++
            };



            if(actString=="ACTION_UP" && moveDown>4){
                yOrigin = (0).toFloat()
                println("====>MOVE :D")
                expandOnBottom()
                moveDown = 0
            }

            if(actString=="ACTION_UP" && moveDown <= 4){
                moveDown=0
                yOrigin = (0).toFloat()
            }

            false
        }

        exo_fullscreen.setOnClickListener {
            expand()
        }


        //fullScreen()
        dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, USER_AGENT))

        back_press.setOnClickListener {
            pause()
            inScreenPlay = false
            playerView.visibility = View.GONE
            title_movie_relative_layout.visibility = View.GONE
            list_movie.visibility = View.VISIBLE
        }


        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }
    }

    fun fullScreen(){
        exo_fullscreen.visibility = View.GONE
        val height = RelativeLayout.LayoutParams.MATCH_PARENT
        val width = RelativeLayout.LayoutParams.MATCH_PARENT
        val layoutParams = RelativeLayout.LayoutParams(width, height)

        playerView.layoutParams =layoutParams

//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        supportActionBar?.hide()
    }

    fun expand() {
        exo_fullscreen.visibility = View.GONE
        if(inScreenPlay) list_movie.visibility = View.GONE
        val height = 650
        val width = RelativeLayout.LayoutParams.MATCH_PARENT
        val layoutParams = RelativeLayout.LayoutParams(width, height)
        playerView.layoutParams =layoutParams
//        window.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
//        supportActionBar?.show()
    }

    fun expandOnBottom(){
        exo_fullscreen.visibility = View.VISIBLE
        val height = 230
        val width = RelativeLayout.LayoutParams.MATCH_PARENT
        val layoutParams = RelativeLayout.LayoutParams(width, height)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        playerView.layoutParams =layoutParams
        list_movie.visibility = View.VISIBLE
    }

    private fun initPlayer(){
        exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }
        playerView.player = exoPlayer
        exoPlayer.addListener(object : Player.EventListener{

        })
    }

    private fun releasePlayer(){
        isPlayerPlaying = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentWindowIndex
        exoPlayer.release()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, exoPlayer.currentWindowIndex)
        outState.putLong(STATE_RESUME_POSITION, exoPlayer.currentPosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            fullScreen()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            expand()
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


    fun playMovie(data:MovieListModel){

        googleAnalytic("play_movie",data.title)
        setActionMovie(data)
        inScreenPlay = true
        expand()
        pause()
        val mineType = getMimeType(data.video_url)
        exoPlayer.clearMediaItems()
        mediaItem = MediaItem.Builder()
            .setUri(data.video_url)
            .setMimeType(mineType)
            .build()
        initPlayer()
        playerView.onResume()

        apiService.addViewer(data)

    }


    private fun setActionMovie(data:MovieListModel){
        movie =data
        playerView.visibility = View.VISIBLE
        title_movie_relative_layout.visibility = View.VISIBLE
        title_movie.text = data.title
        count_live.text = data.count_reaction.toString()+" lượt thích"
        list_movie.visibility = View.GONE

        if(data.isLike){
            not_like_movie.visibility = View.GONE
            like_movie.visibility = View.VISIBLE
        }else{
            not_like_movie.visibility = View.VISIBLE
            like_movie.visibility = View.GONE
        }

    }

    private fun listenerActionMovie(){
        not_like_movie.setOnClickListener {
            movie.isLike = true
            movie.count_reaction = movie.count_reaction+1
            apiService.likeMovie(movie)
            listMovieFragment.setReaction(movie)
            setActionMovie(movie)
        }
//        movie.count_reaction = movie.count_reaction+1



    }

    private fun pause(){
        playerView.onPause()
        exoPlayer.release()
        playerView.onResume()
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            exitProcess(-1)
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


    private fun googleAnalytic(key:String,value:String){
        val android_id = Settings.Secure.getString(
            this.getContentResolver(),
            Settings.Secure.ANDROID_ID
        )

        firebaseAnalytics.logEvent(key) {
            param("user_$android_id",  value)
        }
    }

}
