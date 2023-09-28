package com.ias.gsscore.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.work.WorkManager
import butterknife.BindView
import butterknife.ButterKnife
import coil.load
import com.ias.gsscore.R
import com.ias.gsscore.downloadfileswithworkmanager.CHANNEL_DESC
import com.ias.gsscore.downloadfileswithworkmanager.NOTIFICATION_ID

import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.freeresource.CurrentAffairDetailsResponse
import com.ias.gsscore.network.response.freeresource.FreeResourceDetailsResponse
import com.ias.gsscore.utils.Helpers
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MaterialReportDetailsActivity : BaseActivity() {

    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var post_id=""
    private var type=""
    lateinit var workManager: WorkManager
    @BindView(R.id.img)
    lateinit  var img:ImageView
    @BindView(R.id.desc)
    lateinit  var desc:WebView
    @BindView(R.id.cat)
    lateinit  var cat:TextView
    @BindView(R.id.name)
    lateinit  var name:TextView
    @BindView(R.id.type)
    lateinit  var typename:TextView
    @BindView(R.id.date)
    lateinit  var date:TextView
    @BindView(R.id.download)
    lateinit  var download:LinearLayout
    @BindView(R.id.layoutFooter)
    lateinit var layoutFooter: LinearLayout
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

    @BindView(R.id.reportActivityRootView)
    lateinit var reportActivityRootView: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_report_details)
        ButterKnife.bind(this)
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

        post_id=intent.getStringExtra("post_id")!!
        if(intent.getStringExtra("type_id")!=null) {
            type = intent.getStringExtra("type_id")!!
        }

        loadingDialog = RetrofitHelper.loadingDialog(this)
        workManager = WorkManager.getInstance(this)
        if (Preferences.getInstance(this).frTitle == "") {
            freeResourceDetails(post_id, type)
        } else {
            if (Preferences.getInstance(this).frTitle.split(";")[1] == "false") {
                freeResourceDetails(post_id, type)
            } else {
                currentAffairDetails(post_id, type)
            }
        }


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
                // set toolbar
                setToolBar(response.details!!.details!!.title!!)
                img.load(response.details!!.details!!.banner_url)
               // Glide.with(applicationContext).load(response.details!!.details!!.banner_url).into(img)
                Helpers.setWebViewText(desc,response.details!!.details!!.description!!)
                name.text=response.details!!.details!!.title!!
                if(response.details!!.details!!.category!=null) {
                    cat.text = response.details!!.details!!.category!!
                }
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

    private fun currentAffairDetails(post_id: String, type_id: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(this).userId
        request["post_id"] = post_id
        request["type_id"] = type_id

        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.currentAffairDetails(request)
            val response: CurrentAffairDetailsResponse = result.body()!!
            if (response.status) {
                setToolBar(response!!.details!!.title!!)
                img.load(response.details!!.image_url)
                Helpers.setWebViewText(desc, response.details!!.description!!)
                name.text = response.details!!.title!!
                if(response.details!!.category!=null){
                cat.text=response.details!!.category!!}
                date.text = response.details!!.postedOn!!
                if (!response.details!!.file_url.equals("")) {
                    download.visibility = View.VISIBLE
                } else {
                    download.visibility = View.GONE
                }
                download.setOnClickListener {
                    downLoadFile(
                        response.details!!.file_url!!,
                        response.details!!.title!!,
                        0
                    )
                }

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
            val extension = url?.substring(url.lastIndexOf("."))
            val fileName = title + "pdf" + System.currentTimeMillis() + "." + extension
            CHANNEL_DESC = title
            val requestID = System.currentTimeMillis().toInt()
            NOTIFICATION_ID = requestID
            Helpers.startDownloadingFile(reportActivityRootView,
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

}