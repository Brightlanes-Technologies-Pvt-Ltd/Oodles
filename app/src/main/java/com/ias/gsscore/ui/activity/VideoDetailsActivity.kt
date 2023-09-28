package com.ias.gsscore.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.example.example.VideoList
import com.exoplayer.ExoPlayer2Activity
import com.google.android.exoplayer2.util.Util
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityVideoDetailsBinding
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.adapter.VideoDetailsPagerAdapter
import com.ias.gsscore.ui.adapter.VideosListAdapter
import com.ias.gsscore.ui.viewmodel.VideoDetailsViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory


class VideoDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoDetailsBinding
    lateinit var viewModel: VideoDetailsViewModel
    private val RECOVERY_DIALOG_REQUEST = 1
    var type = ""
    var videoId = ""
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var videosListAdapter: VideosListAdapter? = null
    private var tabName = arrayOf(
        "Comments",
        "Test & Quiz",
        "Resources"
    )
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val playAt =
                    data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
                binding.vimeoPlayerView.seekTo(playAt)

                when (PlayerState.valueOf(data.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE)!!)) {
                    PlayerState.PLAYING -> binding.vimeoPlayerView.play()
                    PlayerState.PAUSED -> binding.vimeoPlayerView.pause()
                    else -> {}
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_details)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[VideoDetailsViewModel::class.java]
        viewModel.context = this
        binding.viewModel = viewModel
        viewModel.lifecycle = lifecycle
        type = intent.getStringExtra("whereFrom").toString()
        videoId = intent.getStringExtra("videoId").toString()
        viewModel.packageId = intent.getStringExtra("packageId").toString()
        viewModel.programId = intent.getStringExtra("programId").toString()
        viewModel.playbackPosition = intent.getLongExtra("PLAY_POSITION",0)
        title = intent.getStringExtra("title").toString()
        binding.headerTitle.text = title

        if(savedInstanceState != null) {
            viewModel.currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            viewModel.playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            viewModel.isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            viewModel.isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }
        initialData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.videoUrl?.let { viewModel.watchVideoOnExoPlayer(it) }
    }

    override fun onDestroy() {
        binding.youtubeView.release()
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        if(Util.SDK_INT > 23) {
            viewModel.releaseExoPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if(Util.SDK_INT <= 23) {
            viewModel.releaseExoPlayer()
        }
    }

    private fun initialData() {
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(this)
        if (type == "My Downloads") {
            binding.layoutTabLayout.visibility = View.GONE
            binding.tvOtherVideo.text = "Other Downloaded Videos"
            binding.tvDelete.text = "Delete"
            binding.icDownloads.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_delete_grey
                )
            )
            linearLayoutManager = LinearLayoutManager(this)
            binding.recyclerView.layoutManager = linearLayoutManager
        } else {
            linearLayoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.layoutManager = linearLayoutManager
            viewModel.videoDetailsApiCall(videoId!!, type, viewModel.packageId, viewModel.programId)
            setTabLayout()
            binding.vimeoPlayerView.setFullscreenClickListener {
                var requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_LANDSCAPE
                resultLauncher.launch(
                    VimeoPlayerActivity.createIntent(
                        this,
                        requestOrientation,
                        binding.vimeoPlayerView
                    )
                )
            }
        }
        binding.ivFullScreen?.setOnClickListener {
            viewModel.launchExoPlayer()
        }
        setVideosAdapter(type)
    }

    private fun setVideosAdapter(type: String) {
        if (type == "My Downloads") {
            var videoList: ArrayList<VideoList> = arrayListOf()
            videosListAdapter =
                VideosListAdapter(
                    this,
                    videoList,
                    type,
                    null,
                    viewModel.packageId
                )
            binding.recyclerView.adapter = videosListAdapter
        }
    }

    private fun setTabLayout() {
        var viewPagerAdapter =
            VideoDetailsPagerAdapter(
                SingletonClass.instance.supportFragmentManager!!,
                lifecycle,
                this,
                "videoDetails",
                3,
                null,
                null
            )
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.tabRippleColor = null
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            Log.e("TAG", "setTabLayout: $position")
            tab.text = tabName[position]
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.exoPlayer?.currentMediaItemIndex?.let {
            outState.putInt(ExoPlayer2Activity.STATE_RESUME_WINDOW,
                it
            )
        }
        viewModel.exoPlayer?.currentPosition?.let {
            outState.putLong(ExoPlayer2Activity.STATE_RESUME_POSITION,
                it
            )
        }
        outState.putBoolean(ExoPlayer2Activity.STATE_PLAYER_FULLSCREEN, viewModel.isFullscreen)
        outState.putBoolean(ExoPlayer2Activity.STATE_PLAYER_PLAYING, viewModel.isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val STATE_RESUME_WINDOW = "resumeWindow"
        const val STATE_RESUME_POSITION = "resumePosition"
        const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
        const val STATE_PLAYER_PLAYING = "playerOnPlay"
    }
}