package com.example.radiofm

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.radiofm.fragments.PostCastMusicFragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

const val HLS_STATIC_URL = "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
const val STATE_RESUME_WINDOW = "resumeWindow"
const val STATE_RESUME_POSITION = "resumePosition"
const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
const val STATE_PLAYER_PLAYING = "playerOnPlay"

class MainActivity : AppCompatActivity() {

    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerView: PlayerView

    private var fragmentIsActive = ""

    private var isPlay = false
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private var mediaItem = MediaItem.Builder()
        .setUri(HLS_STATIC_URL)
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .build()


    lateinit var homeFragment: HomeFragment
    lateinit var postCastMusicFragment: PostCastMusicFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!isOnline(this)){
            Toast.makeText(this,"INTERNET NOT WORKING!",Toast.LENGTH_LONG).show()
            return;
        }


        playerView = findViewById(R.id.player_view)
        playerView.visibility = View.GONE
        dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"))
        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }
        homeFragment(this);
        //postCastMusicFragment(this);
        tv_post_cast_music.setOnClickListener {
            postCastMusicFragment(this)
        }

        tv_radio_fm.setOnClickListener {
            homeFragment(this)
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

    internal fun playRadio(link:String){

        if(isPlay){
            playerView.onPause()
            releasePlayer()
            exoPlayer.clearMediaItems()
        }else{
            playerView.visibility = View.VISIBLE
        }

        mediaItem = MediaItem.Builder()
            .setUri(link)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        initPlayer()
        playerView.onResume()
        isPlay = true

    }

    public fun pauseAudio(){
        playerView.onPause()
        releasePlayer()
    }

    private fun homeFragment(activity: MainActivity){
        controlMusic(false)
        if(fragmentIsActive==="homeFragment") return;
        resetColor()
        tv_radio_fm.setTextColor(resources.getColor(R.color.white))
        homeFragment = HomeFragment(activity)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_main_activity,homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        fragmentIsActive = "homeFragment"
    }

    private fun controlMusic(typeControl:Boolean){
        playerView.setShowPreviousButton(typeControl)
        playerView.setShowFastForwardButton(typeControl)
        playerView.setShowNextButton(typeControl)
        playerView.setShowMultiWindowTimeBar(typeControl)
        playerView.setShowRewindButton(typeControl)
        playerView.setShowMultiWindowTimeBar(false)
    }

    private fun postCastMusicFragment(activity: MainActivity){
        if(fragmentIsActive==="postCastMusicFragment") return;
        resetColor()
        tv_post_cast_music.setTextColor(resources.getColor(R.color.white))

        postCastMusicFragment = PostCastMusicFragment(activity)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_main_activity,postCastMusicFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        fragmentIsActive="postCastMusicFragment"
    }

    private fun resetColor(){
        tv_radio_fm.setTextColor(resources.getColor(R.color.black))
        tv_post_cast_music.setTextColor(resources.getColor(R.color.black))
    }

    internal fun loading(setLoading:Boolean = false){
       if(setLoading){
           loading.visibility = View.VISIBLE
       }else{
           loading.visibility = View.GONE
       }
    }

    private fun initPlayer(){
        exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }
        playerView.player = exoPlayer
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
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, false)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

}
