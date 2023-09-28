package com.ias.gsscore.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.ias.gsscore.R

class ExoPlayer2Activity : AppCompatActivity() {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: StyledPlayerView
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private var mediaItem : MediaItem? = null

    var packageId = ""
    var type = ""
    var videoId = ""
    var programId = ""
    var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        playerView = findViewById(R.id.player_view)

        if(savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }

          mediaItem = MediaItem.Builder()
            .setUri(intent.getStringExtra("VIDEO_URL"))
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        playbackPosition = intent.getLongExtra("PLAY_POSITION",0)
        packageId = intent.getStringExtra("packageId").toString()
        type = intent.getStringExtra("whereFrom").toString()
        videoId = intent.getStringExtra("videoId").toString()
        programId = intent.getStringExtra("programId").toString()
        title = intent.getStringExtra("title").toString()

    }

    private fun initPlayer() {
            exoPlayer = ExoPlayer.Builder(this).build().apply {
                playWhenReady = isPlayerPlaying
                seekTo(currentWindow, playbackPosition)
                mediaItem?.let { it1 -> setMediaItem(it1, false) }
                prepare()
            }
            playerView.player = exoPlayer
    }

    private fun releasePlayer() {
        isPlayerPlaying = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentMediaItemIndex
        exoPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, exoPlayer.currentMediaItemIndex)
        outState.putLong(STATE_RESUME_POSITION, exoPlayer.currentPosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if(Util.SDK_INT > 23) {
            initPlayer()
            playerView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if(Util.SDK_INT <= 23) {
            initPlayer()
            playerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if(Util.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if(Util.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        releasePlayer()
        val intent = Intent(this@ExoPlayer2Activity, VideoDetailsActivity::class.java)
        intent.putExtra("whereFrom",type)
        intent.putExtra("videoId", videoId)
        intent.putExtra("packageId",packageId)
        intent.putExtra("programId",programId)
        intent.putExtra("title",title)
        intent.putExtra("PLAY_POSITION", playbackPosition)
        startActivity(intent)
        finish()
    }

    companion object {
        const val STATE_RESUME_WINDOW = "resumeWindow"
        const val STATE_RESUME_POSITION = "resumePosition"
        const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
        const val STATE_PLAYER_PLAYING = "playerOnPlay"
    }
}
