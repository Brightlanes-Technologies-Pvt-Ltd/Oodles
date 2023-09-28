package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import coil.load
import com.example.example.PackageProgramDetails
import com.example.example.VideoList
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.ias.gsscore.databinding.ActivityVideoDetailsBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.myaccount.VideoDetails
import com.ias.gsscore.network.response.myaccount.VideoDetailsResponse
import com.ias.gsscore.ui.activity.ExoPlayer2Activity
import com.ias.gsscore.ui.activity.VideoWebViewActivity
import com.ias.gsscore.ui.adapter.OtherVideosListAdapter
import com.ias.gsscore.utils.Preferences
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import us.zoom.sdksample.ui.InitAuthSDKActivity


@SuppressLint("StaticFieldLeak")
class VideoDetailsViewModel(binding: ViewDataBinding) : ViewModel() {
    lateinit var childFragmentManager: FragmentManager
    var binding = binding as ActivityVideoDetailsBinding
    var youtubeId = "n261iHgD1rs"

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    var isVideoYoutube: ObservableInt = ObservableInt(0)
    var otherVideosListAdapter = OtherVideosListAdapter()
    lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var videoDetails: VideoDetails? = null
    lateinit var lifecycle: Lifecycle
    var exoPlayerView: StyledPlayerView? = null
    var exoPlayer: ExoPlayer? = null
    var isPlayerPlaying = true
    private var mediaItem : MediaItem? = null
    var currentWindow = 0
    var playbackPosition: Long = 0
    var isFullscreen = false
    var videoUrl : String? = null
    var packageId = ""
    var programId = ""

    fun backPress() {
        (context as Activity).onBackPressed()
    }

    fun videoDetailsApiCall(videoId: String, type: String, packageId: String, programId: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["video_id"] = videoId
        request["package_id"] = packageId
        request["program_id"] = programId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.videoDetails(request)
            val response: VideoDetailsResponse = result.body()!!
            if (response.status) {
                videoDetails = response.videoDetails
                setDataVideoDetails(
                    videoDetails!!,
                    response.relatedVideo,
                    type,
                    response.packageProgramDetails
                )
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

    private fun setDataVideoDetails(
        videoDetails: VideoDetails,
        relatedVideo: ArrayList<VideoList>,
        type: String,
        packageProgramDetails: ArrayList<PackageProgramDetails>
    ) {
        binding.tvTitle.text = videoDetails.title
        binding.tvDescription.text = videoDetails.description
        binding.tvDuration.text = videoDetails.duration
        binding.start.text = videoDetails.startDate
        binding.tvThoughtBy.text = videoDetails.toughtBy
        otherVideosListAdapter.update(relatedVideo, context, type, packageProgramDetails)
        binding.videoThumbnail.load(videoDetails.thumbnail)
         when {
             videoDetails.videoType.equals("1") -> {
                 //Youtube
                 isVideoYoutube.set(0)
                 watchYoutubeVideo(videoDetails.videoUrlId!!)
             }
             videoDetails.videoType.equals("2") -> {
                 //Exo player
                 isVideoYoutube.set(3)
                 videoUrl = videoDetails.videoUrl
                 videoUrl?.let { watchVideoOnExoPlayer(it) }
             }
             videoDetails.videoType.equals("7") -> {
                 //zoom meeting
                 isVideoYoutube.set(2)
                 binding.videoThumbnail.load(videoDetails.thumbnail)
             }
             videoDetails.videoType.equals("5") -> {
                 //Exo player
                 isVideoYoutube.set(3)
                 videoUrl = videoDetails.videoUrlId
                 videoUrl?.let { watchVideoOnExoPlayer(it) }
             }
             else -> {
                 //Edugyaan
                 binding.videoThumbnail.load(videoDetails.thumbnail)
                 isVideoYoutube.set(2)
             }
         }
    }

    fun clickPlay() {
        if (videoDetails?.videoType.equals("7", true)) {
            getJWT(videoDetails!!)
        }  else {
            val intent = Intent(context, VideoWebViewActivity::class.java)
            intent.putExtra("title", videoDetails!!.title)
            intent.putExtra("url", videoDetails!!.videoUrl)
            context.startActivity(intent)
        }
    }

    /**
     * Launch Exoplayer in full screen
     */
    fun launchExoPlayer() {
        releaseExoPlayer()
        val intent = Intent(context, ExoPlayer2Activity::class.java)
        intent.putExtra("VIDEO_URL", videoUrl)
        intent.putExtra("PLAY_POSITION", playbackPosition)
        intent.putExtra("whereFrom",videoDetails?.videoType)
        intent.putExtra("videoId", videoDetails?.video_id)
        intent.putExtra("packageId",packageId)
        intent.putExtra("programId",programId)
        intent.putExtra("title",videoDetails?.title!!)
        context.startActivity(intent)

    }

    private fun setupVimeoView(videoUrlId: String?) {
        //  https://player.vimeo.com/external/684553812.m3u8?s=942a7042191491c500c7a0a62bc8500c1bbf9370
        lifecycle.addObserver(binding.vimeoPlayerView)
        binding.vimeoPlayerView.initialize(true, videoUrlId!!.toInt()/*59777392*/)
        binding.vimeoPlayerView.setFullscreenVisibility(true)
    }

    private fun launchZoomMeeting(videoDetails: VideoDetails?, jwt: String) {
        val intent = Intent(context, InitAuthSDKActivity::class.java)
        intent.putExtra("ZOOM_MEETING_ID", videoDetails?.zoomMeetingId)
         intent.putExtra("ZOOM_MEETING_PWD",videoDetails?.zoomMeetingPwd)
         intent.putExtra("VIDEO_URL_ID",videoDetails?.videoUrlId)
         intent.putExtra("USER_NAME",Preferences.getInstance(context).userName)
         intent.putExtra("JWT", jwt)
        context.startActivity(intent)
    }

    private fun getJWT(videoDetails: VideoDetails) {
        coroutineScope.launch {
            loadingDialog.show()
            val requestBody = HashMap<String, Any>()
            requestBody["meetingNumber"] = videoDetails.zoomMeetingId!!
            requestBody["role"] = 1
            val result = apiService.getJWT(requestBody)
            if (result != null) {
                val jwt = result["signature"]
                launchZoomMeeting(videoDetails, jwt!!)
            } else {
                Toast.makeText(
                    context,
                    "Server Error!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun watchYoutubeVideo(videoUrlId: String) {
        /*  binding.youtubeView.enterFullScreen();
          binding.youtubeView.toggleFullScreen();*/
        lifecycle.addObserver(binding.youtubeView)
        binding.youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // loading the selected video into the YouTube Player
                youTubePlayer.cueVideo(videoUrlId, 0F)
            }
        })
    }

    /**
     * Watch video on exoplayer
     */
    fun watchVideoOnExoPlayer(videoUrl : String) {
        Log.d("video url====== exo  ", videoUrl.toString())
        if (videoDetails != null) {
            mediaItem = MediaItem.Builder()
                .setUri(videoUrl)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()

            exoPlayerView = binding.playerView
            exoPlayerView?.visibility = View.VISIBLE
            initExoPlayer()
        }
    }

    /**
     * Init exoplayer
     */
     private fun initExoPlayer() {
         exoPlayer = ExoPlayer.Builder(context).build().apply {
             playWhenReady = isPlayerPlaying
             seekTo(currentWindow, playbackPosition)
             mediaItem?.let { it1 -> setMediaItem(it1, false) }
             prepare()
         }
         exoPlayerView?.player = exoPlayer
         exoPlayerView?.useArtwork = true
         exoPlayerView?.controllerAutoShow = true

         exoPlayer?.addListener(object : Player.Listener {
             override fun onPlayerError(error: PlaybackException) {
                 super.onPlayerError(error)
                 Log.e("ExoPlayer", "Error occurred==1: $error")
             }

             override fun onPlayerErrorChanged(error: PlaybackException?) {
                 super.onPlayerErrorChanged(error)
                 Log.e("ExoPlayer", "Error occurred==2: $error")
             }
         })

     }

    /**
     * Release the Exoplayer
     */
    fun releaseExoPlayer() {
        exoPlayerView?.onPause()
        if(exoPlayer?.currentPosition !=null) {
            playbackPosition = exoPlayer?.currentPosition!!
        }
        if(exoPlayer?.currentMediaItemIndex!=null) {
            currentWindow = exoPlayer?.currentMediaItemIndex!!
        }
        exoPlayer?.release()
    }
}