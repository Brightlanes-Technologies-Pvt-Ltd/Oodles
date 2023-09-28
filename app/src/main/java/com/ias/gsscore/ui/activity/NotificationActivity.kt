package com.ias.gsscore.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.ias.gsscore.R
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.notification.NotificationList
import com.ias.gsscore.network.notification.NotificationResponse
import com.ias.gsscore.ui.adapter.NotificationAdapter
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException

class NotificationActivity : AppCompatActivity() {
    @BindView(R.id.rvNoti)
    lateinit var rvNoti: RecyclerView
    @BindView(R.id.back)
    lateinit var back: LinearLayout
    private var notificationAdapter: NotificationAdapter?=null
    private lateinit var linearLayoutManager : LinearLayoutManager
    lateinit var loadingDialog: AlertDialog
    val viewModelJob = Job()
    var pageNo = 1
    var limit = 10
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var loading = true
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var notificationList: MutableList<NotificationList> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        ButterKnife.bind(this)
        loadingDialog = RetrofitHelper.loadingDialog(this)
        setMaterialAdapter()
        back.setOnClickListener{
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }
    }


    private fun setMaterialAdapter() {
        notification()
        linearLayoutManager = LinearLayoutManager(this)
        rvNoti.layoutManager = linearLayoutManager
        notificationAdapter = NotificationAdapter(this,notificationList)
        rvNoti.adapter = notificationAdapter

        rvNoti.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisiblesItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            try {
                                loading = false
                                pageNo += 1
                                notification()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        })


    }

    private fun notification() {
        var request: java.util.HashMap<String, String> = java.util.HashMap()
        request["user_id"] = Preferences.getInstance(this).userId

        request["page"] = ""+pageNo


        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.notification(request)
            val response: NotificationResponse = result.body()!!
            if (response.status) {

                if (pageNo==1){
                    if (response.list!!.isNotEmpty()){
                        notificationList.clear()
                        notificationList.addAll(response.list!!)
                        notificationAdapter!!.notifyDataSetChanged()
                    }

                }else{
                    loading = if (response.list!!.isNotEmpty()){
                        notificationList.addAll(response.list!!)
                        notificationAdapter!!.notifyDataSetChanged()
                        true
                    }else{
                        false;
                    }
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
    }
}