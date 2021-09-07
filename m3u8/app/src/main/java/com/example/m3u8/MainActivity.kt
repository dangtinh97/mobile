package com.example.m3u8

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var player:SimpleExoPlayer ? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playBackPosition : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var hlsUri = "https://player.vimeo.com/external/590874957.m3u8?s=c92f6b15e812f2628b804ab4e465278fe00e6fd2&oauth2_token_id=1524311308";
//        val hlsUri = "https://player.vimeo.com/external/590874957.hd.mp4?s=63b003cb92f89240e201d77633d2b855e6e9e518&profile_id=703&oauth2_token_id=1524311308";
//        val hlsUri = "https://196vod-adaptive.akamaized.net/exp=1629722163~acl=%2F2b3f2af2-19b0-4301-9e01-4dac4f2ca09b%2F%2A~hmac=ffaba339d2147bd0f8ba58005a52f60adf5474f2e4406c8bdc92436c0d86121c/2b3f2af2-19b0-4301-9e01-4dac4f2ca09b/parcel/video/0dc73b0d.mp4?range=2289-248576";
        val uri:Uri = Uri.parse(hlsUri)
        //val exoPlayerView =  ExoPlayerView(this)
        exoPlayerView.prepare(uri)

    }




    override fun onPause() {
        super.onPause()
        exoPlayerView.onPause()
    }

    override fun onResume() {
        super.onResume()
        exoPlayerView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerView.onDestroy()
    }
}
