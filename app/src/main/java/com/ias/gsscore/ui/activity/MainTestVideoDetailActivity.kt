package com.ias.gsscore.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityMainTestVideoDeatailBinding
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.viewmodel.MainsTestVideoDetailsViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class MainTestVideoDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainTestVideoDeatailBinding
    lateinit var viewModel: MainsTestVideoDetailsViewModel

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            var playAt =
                data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
            binding.vimeoPlayerView.seekTo(playAt)

            var playerState =
                PlayerState.valueOf(data.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE)!!)
            when (playerState) {
                PlayerState.PLAYING -> binding.vimeoPlayerView.play()
                PlayerState.PAUSED -> binding.vimeoPlayerView.pause()
                else -> {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_test_video_deatail)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ActivityViewModelFactory(binding,
            SingletonClass.instance)
        )[MainsTestVideoDetailsViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.context = this
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(this)
        val title = intent.getStringExtra("title")
        val testId = intent.getStringExtra("testId").toString()
        val videoId = intent.getStringExtra("videoId").toString()
        binding.tvTitle.text = title
        viewModel.supportFragmentManager = supportFragmentManager
        viewModel.lifecycle = lifecycle
        binding.ivBack.setOnClickListener { finish() }
        viewModel.videoDetailsApiCall(videoId,testId)

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

    override fun onDestroy() {
        binding.youtubeView.release()
        super.onDestroy()
    }

}