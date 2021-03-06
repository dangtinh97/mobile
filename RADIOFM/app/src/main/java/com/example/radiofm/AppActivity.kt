package com.example.radiofm

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.radiofm.adapters.AppViewPagerAdapter
import com.example.radiofm.fragments.AccountFragment
import com.example.radiofm.fragments.HomeAppFragment
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
import kotlinx.android.synthetic.main.activity_app.*
import java.util.*
import kotlin.system.exitProcess

const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"
//link huong dan https://github.com/yoobi/exoplayer-kotlin
//https://exoplayer.dev/hello-world.html

class AppActivity : AppCompatActivity() {
    private var pressedTime: Long = 0

    private var playRadio:Boolean = false
    private var playSong:Boolean = false

    private lateinit var homeAppFragment: HomeAppFragment

    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var player: SimpleExoPlayer

    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerView: PlayerView

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true

    private lateinit var android_id:String

    private var mediaItem = MediaItem.Builder()
        .setUri(HLS_STATIC_URL)
        .setMimeType(MimeTypes.APPLICATION_M3U8)
        .build()

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        player = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
        }
        exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
        }
        if(!isOnline(this)){
            return Toast.makeText(this,"Vui l??ng k???t n???i internet",Toast.LENGTH_LONG).show()
        }

        android_id = Settings.Secure.getString(
            this.getContentResolver(),
            Settings.Secure.ANDROID_ID
        )

        firebaseAnalytics = Firebase.analytics

        homeAppFragment = HomeAppFragment(this)

        val adapter = AppViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(homeAppFragment,"Trang ch???")
        adapter.addFragment(AccountFragment(this),"T??i kho???n")
        app_viewpager.adapter = adapter

        /**EXO*/
        playerView = findViewById(R.id.player_view)
        dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, USER_AGENT))

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }
        /**end set EXO*/

        app_viewpager.setCurrentItem(0);

        menu_app.setOnNavigationItemSelectedListener{item->

            if(item.itemId===R.id.home_menu && (playRadio || playSong)) {
                main_media_frame.visibility = View.VISIBLE
            }else{
                main_media_frame.visibility = View.GONE
            }

            when(item.itemId){
                R.id.home_menu->app_viewpager.setCurrentItem(0)
                R.id.account_menu-> app_viewpager.setCurrentItem(1)
            }
            true
        }
        //notification()
    }

    //EXO PLAYER

    fun playMusic(link:String,controlMusic:Boolean){
        val mineType = getMimeType(link)
        controlMusic(controlMusic)
        pause()
        if(controlMusic){
            firebaseAnalytics.logEvent("listen_music") {
                param("user_$android_id",  Calendar.getInstance().getTime().toString())
            }
        }else{
            firebaseAnalytics.logEvent("listen_radio") {
                param("user_$android_id",  Calendar.getInstance().getTime().toString())
            }
        }

        main_media_frame.visibility = View.VISIBLE

        if(controlMusic){
            if(playRadio) player.clearMediaItems()
            mediaItem = MediaItem.Builder()
                .setUri(link)
                .setMimeType(mineType)
                .build()
            initPlayerSong(mediaItem)
            playSong = true
        }else{

            mediaItem = MediaItem.Builder()
                .setUri(link)
                .setMimeType(mineType)
                .build()

            initPlayer(mediaItem)

            playRadio = true
        }

        playerView.onResume()

    }

    fun getUserApp(): String {
        return this.android_id
    }

    private fun initPlayer(mediaItem: MediaItem){
        exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }
        playerView.player = exoPlayer
    }

    private fun initPlayerSong(mediaItem:MediaItem){
        player = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }


        player.addListener(object : Player.EventListener {


            override fun onEvents(player: Player, events: Player.Events) {
                println("onEvents"+player.currentManifest)


                if(events.contains(Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED)){

                }

                super.onEvents(player, events)
            }
//
//            override fun onIsPlayingChanged(isPlaying: Boolean) {
//                println("====>$isPlaying")
//                super.onIsPlayingChanged(isPlaying)
//            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                homeAppFragment.setUrlIsPlay(mediaItem?.playbackProperties?.uri.toString())
                println("onMediaItemTransition:"+mediaItem?.playbackProperties?.uri)
                super.onMediaItemTransition(mediaItem, reason)
            }


            override fun onPlayerStateChanged(playWhenReady: Boolean,playbackState: Int) {



                println("onPlayerStateChanged$playbackState")

                when (playbackState) {
                    Player.STATE_IDLE -> {
                        Log.d(TAG, "onPlayerStateChanged: STATE_IDLE")
                    }
                    Player.STATE_BUFFERING -> {
                        Log.d(TAG, "onPlayerStateChanged: STATE_BUFFERING")
                    }
                    Player.STATE_READY -> {
                        Log.d(TAG, "onPlayerStateChanged: STATE_READY")
                    }
                    Player.STATE_ENDED -> {
                        Log.d(TAG, "onPlayerStateChanged: STATE_ENDED")
                    }
                }
            }
        })

        playerView.player = player
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, exoPlayer.currentWindowIndex)
        outState.putLong(STATE_RESUME_POSITION, exoPlayer.currentPosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    private fun pause(){
        playerView.onPause()

        if(playSong) player.release()
        if(playRadio) exoPlayer.release()
        playerView.onResume()
    }

    private fun controlMusic(typeControl:Boolean){
        playerView.setShowPreviousButton(typeControl)
        playerView.setShowFastForwardButton(typeControl)
        playerView.setShowNextButton(typeControl)
        playerView.setShowMultiWindowTimeBar(typeControl)
        playerView.setShowRewindButton(typeControl)
        playerView.setShowMultiWindowTimeBar(false)
    }


    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun setMedia(link:String,setURL:Boolean){
        player.addMediaItem(MediaItem.fromUri(link))
        if(setURL) player.setMediaItem(MediaItem.fromUri(link))
    }

    //end EXO PLAYER

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

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            exitProcess(-1)
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}
