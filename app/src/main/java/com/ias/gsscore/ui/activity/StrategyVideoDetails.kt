package com.ias.gsscore.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import butterknife.BindView
import butterknife.ButterKnife
import com.ias.gsscore.R
import com.ias.gsscore.downloadfileswithworkmanager.CHANNEL_DESC
import com.ias.gsscore.downloadfileswithworkmanager.NOTIFICATION_ID
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.freeresource.FreeResourceDetailsResponse
import com.ias.gsscore.ui.adapter.OtherStrategyVideosListAdapter
import com.ias.gsscore.utils.Helpers
import com.ias.gsscore.utils.Preferences
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StrategyVideoDetails : BaseActivity() {
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var post_id=""
    private var type=""
    lateinit var workManager: WorkManager
    @BindView(R.id.tvTitle)
    lateinit  var tvTitle: TextView
    @BindView(R.id.layoutFooter)
    lateinit var layoutFooter: LinearLayout
    @BindView(R.id.typename)
    lateinit  var typename: TextView
    @BindView(R.id.date)
    lateinit  var date: TextView
    @BindView(R.id.download)
    lateinit  var download: LinearLayout
    @BindView(R.id.videoThumbnail)
    lateinit  var videoThumbnail: ImageView
    @BindView(R.id.youtubeView)
    lateinit  var youtubeView: YouTubePlayerView
    @BindView(R.id.recyclerView)
    lateinit  var recyclerView: RecyclerView
    @BindView(R.id.course_layout)
    lateinit var course_layout: LinearLayout

    @BindView(R.id.study_layout)
    lateinit var study_layout: LinearLayout

    @BindView(R.id.category_layout)
    lateinit var category_layout: LinearLayout

    @BindView(R.id.materialLayout)
    lateinit var materialLayout: LinearLayout

    @BindView(R.id.account_layout)
    lateinit var account_layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strategy_video_details)
        ButterKnife.bind(this)
        post_id=intent.getStringExtra("post_id")!!
        type=intent.getStringExtra("type_id")!!

        loadingDialog = RetrofitHelper.loadingDialog(this)
        workManager = WorkManager.getInstance(this)
        course_layout.setOnClickListener{
            var intent= Intent(this,MainActivity::class.java)
            intent.putExtra("fromEdit",true)
            intent.putExtra("pos","1")
            startActivity(intent)

        }
        study_layout.setOnClickListener{
            var intent= Intent(this,MainActivity::class.java)
            intent.putExtra("fromEdit",true)
            intent.putExtra("pos","2")
            startActivity(intent)

        }

        category_layout.setOnClickListener{
            var intent= Intent(this,MainActivity::class.java)
            intent.putExtra("fromEdit",true)
            intent.putExtra("pos","3")
            startActivity(intent)

        }

        materialLayout.setOnClickListener{
            var intent= Intent(this,MainActivity::class.java)
            intent.putExtra("fromEdit",true)
            intent.putExtra("pos","4")
            startActivity(intent)

        }
        account_layout.setOnClickListener{
            var intent=Intent(this,MainActivity::class.java)
            intent.putExtra("fromEdit",true)
            intent.putExtra("pos","5")
            startActivity(intent)

        }

        freeResourceDetails(post_id,type)
    }

    private fun freeResourceDetails(post_id: String,type_id:String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(this).userId
        request["post_id"] = post_id
        request["type_id"] = type_id



        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.freeResourceDetails(request)
            val response: FreeResourceDetailsResponse = result.body()!!
            if (response.status) {
                setToolBar(response.details!!.details!!.title!!)
                //Glide.with(applicationContext).load(response.details!!.details!!.meta_image).into(videoThumbnail)
               // Helpers.setWebViewText(desc,response.details!!.details!!.description!!)
                tvTitle.text=response.details!!.details!!.title!!
                //cat.text=response.details!!.details!!.category!!
                typename.text=response.details!!.details!!.type_name!!
                date.text=response.details!!.details!!.postedOn!!
                if( !response.details!!.details!!.file_url!!.equals("")){
                    download.visibility= View.VISIBLE
                }else{
                    download.visibility= View.GONE
                }
                download.setOnClickListener {
                    downLoadFile(
                        response.details!!.details!!.file_url!!,
                        response.details!!.details!!.title!!,
                        0
                    )
                }

                lifecycle.addObserver(youtubeView)

               // youtubeView.enterFullScreen()
                youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        // loading the selected video into the YouTube Player
                        youTubePlayer.cueVideo(response.details!!.details!!.video_code!!, 0F)

                    }
                })

                videoThumbnail.setOnClickListener {
                   /* var intent=Intent(applicationContext,YouTubeVideoActivity::class.java)
                    intent.putExtra("id",response.details!!.details!!.video_code!!)
                    startActivity(intent)*/

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v="+response.details!!.details!!.video_code!!)
                    )
                    intent.putExtra("force_fullscreen", true)
                    startActivity(intent)

                }

                var linearLayoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = linearLayoutManager
              var  otherStrategyVideosListAdapter = OtherStrategyVideosListAdapter( applicationContext,
                  response.details!!.related_articles
              )
                recyclerView.adapter = otherStrategyVideosListAdapter

            } else {
                Toast.makeText(
                    applicationContext,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    fun downLoadFile(url: String, title: String, position: Int) {
        if (url.isNotEmpty()) {
            val extension = url.substring(url.lastIndexOf("."))
            val fileName = title + "pdf" + System.currentTimeMillis() + "." + extension
            CHANNEL_DESC = title
            val requestID = System.currentTimeMillis().toInt()
            NOTIFICATION_ID = requestID
            Helpers.startDownloadingFile(null,
                this,
                url,
                fileName,
                "PDF",
                workManager,
                this
            )
        } else
            Toast.makeText(this, "URL not found!", Toast.LENGTH_SHORT).show()
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