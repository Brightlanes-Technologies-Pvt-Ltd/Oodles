package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ias.gsscore.databinding.ActivityMainTestVideoDeatailBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.myaccount.QuestionData
import com.ias.gsscore.network.response.testresult.TestResultResponse
import com.ias.gsscore.ui.adapter.VideoDetailsPagerAdapter
import com.ias.gsscore.utils.Preferences
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainsTestVideoDetailsViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as ActivityMainTestVideoDeatailBinding

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    var isVideoYoutube: ObservableInt = ObservableInt(0)
    lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var videoDetails: QuestionData? = null
    lateinit var supportFragmentManager: FragmentManager

    @SuppressLint("StaticFieldLeak")
    lateinit var lifecycle: Lifecycle
    private var tabName = arrayOf(
        "Question & Solution",
        "Other Videos",
        "Comments"
    )

    fun videoDownload() {
        if (!videoDetails!!.videoUrl!!.contains("http")) {
            Toast.makeText(
                context,
                "URL not found",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun videoDetailsApiCall(
        videoId: String,
        testId: String
    ) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["test_id"] = testId
        request["video_id"] = videoId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.mainsQuestionVideo(request)
            val response: TestResultResponse = result.body()!!
            if (response.status) {
                if (response.videoDetails.size > 0) {
                    videoDetails = response.videoDetails[0]!!
                    setVideoDetails(response)
                } else
                    Toast.makeText(
                        context,
                        response.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun setVideoDetails(response: TestResultResponse) {
        if (response.videoDetails.size > 0) {
            if (videoDetails!!.videoType.equals("1")) {
                isVideoYoutube.set(0)
                watchYoutubeVideo(videoDetails!!.videoUrlId!!)
            } else {
                isVideoYoutube.set(1)
                setupVimeoView(videoDetails!!.videoUrlId)
            }
        }
        setTabLayout(response)
    }

    private fun setupVimeoView(videoUrlId: String?) {
        //  https://player.vimeo.com/external/684553812.m3u8?s=942a7042191491c500c7a0a62bc8500c1bbf9370
        lifecycle.addObserver(binding.vimeoPlayerView)
        binding.vimeoPlayerView.initialize(true, videoUrlId!!.toInt())//59777392)
        binding.vimeoPlayerView.setFullscreenVisibility(true)
    }

    private fun watchYoutubeVideo(videoUrlId:String) {
        lifecycle.addObserver(binding.youtubeView)
        binding.youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // loading the selected video into the YouTube Player
                youTubePlayer.cueVideo(videoUrlId, 0F)
            }
        })
    }

    private fun setTabLayout(response: TestResultResponse) {
        var viewPagerAdapter =
            VideoDetailsPagerAdapter(
                supportFragmentManager,
                lifecycle,
                context,
                "mainVideoDetails",
                3,
                null,
                response

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

    /* private fun watchYoutubeVideo(questionData: QuestionData) {
         val id = questionData!!.videoUrl
         if(!id.equals("")){
             val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
             val webIntent = Intent(
                 Intent.ACTION_VIEW,
                 Uri.parse("http://www.youtube.com/watch?v=$id")
             )
             try {
                 startActivity(appIntent)
             } catch (ex: ActivityNotFoundException) {
                 startActivity(webIntent)
             }
         }
     }*/

}