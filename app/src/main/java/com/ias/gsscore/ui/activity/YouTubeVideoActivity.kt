package com.ias.gsscore.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import butterknife.BindView
import com.ias.gsscore.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YouTubeVideoActivity : BaseActivity() {
    @BindView(R.id.youtubeView)
    lateinit  var youtubeView: YouTubePlayerView
    var id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_tube_video)
        id=intent.getStringExtra("id")!!

        youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // loading the selected video into the YouTube Player
                youTubePlayer.cueVideo(id, 0F)

            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            youtubeView.enterFullScreen()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            youtubeView.exitFullScreen()
        }
    }
}