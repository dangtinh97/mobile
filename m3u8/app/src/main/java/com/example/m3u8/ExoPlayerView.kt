package com.example.m3u8

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

class ExoPlayerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), Player.EventListener {

    private var simpleExoPlayer: SimpleExoPlayer

    init {
        val playerView = PlayerView(context)
        addView(playerView)

        simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        simpleExoPlayer.addListener(this)

        simpleExoPlayer.playWhenReady = true

        playerView.player = simpleExoPlayer
    }

    fun prepare(uri: Uri){
        val timeout = 1800000
        val dataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(context, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"),
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            timeout,
            true)

        val mediaSource = when(Util.inferContentType(uri)){
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
            else -> throw Exception("Fuente desconocida")
        }.createMediaSource(uri)

        simpleExoPlayer.prepare(mediaSource)
    }

    fun onPause(){
        simpleExoPlayer.playWhenReady = false
    }

    fun onResume(){
        simpleExoPlayer.playWhenReady = true
    }

    fun onDestroy(){
        simpleExoPlayer.release()
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        Log.e("ExoPlayer", "Error: ", error)
    }
}
