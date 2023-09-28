package com.ias.gsscore.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.example.example.VideoList
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityVideoDetailsBinding
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.ui.adapter.VideoDetailsPagerAdapter
import com.ias.gsscore.ui.adapter.VideosListAdapter
import com.ias.gsscore.ui.viewmodel.VideoDetailsViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory


class VideoDetailsFragment(
    context: Context,
    type: String,
    private val videoId: String?,
    private val packageId: String,
    private val programId: String
) : Fragment() {
    private val RECOVERY_DIALOG_REQUEST = 1
    lateinit var binding: ActivityVideoDetailsBinding
    lateinit var viewModel: VideoDetailsViewModel
    val type = type
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var videosListAdapter: VideosListAdapter? = null
    var materialTopList = ArrayList<MaterialResponse>()
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

                when (PlayerState.valueOf(data!!.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE)!!)) {
                    PlayerState.PLAYING -> binding.vimeoPlayerView.play()
                    PlayerState.PAUSED -> binding.vimeoPlayerView.pause()
                    else -> {}
                }
            }
        }

    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.activity_video_details, container, false)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, SingletonClass.instance)
        )[VideoDetailsViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.context = requireContext()
        viewModel.lifecycle = lifecycle
      //  requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        viewModel.childFragmentManager = childFragmentManager
        initialData()
        return binding.root;

    }

    private fun initialData() {
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(requireContext())
        if (type == "My Downloads") {
            binding.layoutTabLayout.visibility = View.GONE
            binding.tvOtherVideo.text = "Other Downloaded Videos"
            binding.tvDelete.text = "Delete"
            binding.icDownloads.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_delete_grey
                )
            )
            linearLayoutManager = LinearLayoutManager(context)
            binding.recyclerView.layoutManager = linearLayoutManager
        } else {
            linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerView.layoutManager = linearLayoutManager
            viewModel.videoDetailsApiCall(videoId!!, type, packageId, programId)
            setTabLayout()
            binding.vimeoPlayerView.setFullscreenClickListener {
                var requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_AUTO
                resultLauncher.launch(
                    VimeoPlayerActivity.createIntent(
                        context,
                        requestOrientation,
                        binding.vimeoPlayerView
                    )
                )
            }
        }
        setVideosAdapter(type)
    }

    private fun setVideosAdapter(type: String) {
        if (type == "My Downloads") {
            var videoList: ArrayList<VideoList> = arrayListOf()
            videosListAdapter =
                context?.let {
                    VideosListAdapter(
                        requireContext(),
                        videoList,
                        type,
                        null,
                        packageId
                    )
                }
            binding.recyclerView.adapter = videosListAdapter
        }
    }

    private fun setTabLayout() {
        var viewPagerAdapter =
            VideoDetailsPagerAdapter(
                SingletonClass.instance.supportFragmentManager!!,
                lifecycle,
                requireContext(),
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

    /*  override fun onInitializationSuccess(
          p0: YouTubePlayer.Provider?,
          player: YouTubePlayer?,
          wasRestored: Boolean
      ) {
          if (!wasRestored) {

              // loadVideo() will auto play video
              // Use cueVideo() method, if you don't want to play it automatically
              player!!.cueVideo("https://www.youtube.com/watch?v=0jqikf3IX3g")

              // Hiding player controls
              player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
          }
      }

      override fun onInitializationFailure(
          p0: YouTubePlayer.Provider?,
          errorReason: YouTubeInitializationResult?
      ) {
          if (errorReason!!.isUserRecoverableError) {
              errorReason!!.getErrorDialog(
                  requireActivity(),
                  RECOVERY_DIALOG_REQUEST
              ).show()
          } else {
              val errorMessage = String.format(
                  getString(R.string.error_player), errorReason.toString()
              )
              Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
          }
      }*/



}
