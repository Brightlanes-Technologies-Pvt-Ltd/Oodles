package com.ias.gsscore.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
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
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.network.response.freeresource.FreeResourceList
import com.ias.gsscore.network.response.freeresource.FreeResourceResponse
import com.ias.gsscore.ui.adapter.BooksDownloadListAdapter
import com.ias.gsscore.utils.Helpers
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.HashMap

class BooksDownloadFragment(context: Context,type1:String) : Fragment(),BooksDownloadListAdapter.DownloadClick {
    private val type = type1;
    @BindView(R.id.rvDownload)
    lateinit var rvDownload: RecyclerView
    @BindView(R.id.ivLayout)
    lateinit var ivLayout: LinearLayout
    @BindView(R.id.tvName)
    lateinit var tvName: AppCompatTextView
    @BindView(R.id.tvYear)
    lateinit var tvYear: AppCompatTextView
    lateinit var workManager: WorkManager
    private lateinit var downloadClickContext: BooksDownloadListAdapter.DownloadClick
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var dataList = ArrayList<MaterialResponse>();
    private var listAdapter: BooksDownloadListAdapter? = null
    var pageNo = 0
    var limit = 10
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var loading = true
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var packageList: MutableList<FreeResourceList> = arrayListOf()

    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_books_download, container, false)
        ButterKnife.bind(this, view);
        downloadClickContext = this
        workManager = WorkManager.getInstance(requireActivity())
        loadingDialog = RetrofitHelper.loadingDialog(requireContext())
        return view;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setMaterialAdapter()
    }

    private fun setMaterialAdapter() {
        freeResource1(MaterialFragment.post_type,MaterialFragment.cat_slug)
        if (type=="NCERT/NOIS Books"){
            tvYear.text = "NCERT Books"
            ivLayout.visibility = View.GONE
            tvName.visibility = View.GONE
        }
        else{
            ivLayout.visibility = View.GONE
            tvName.visibility = View.GONE
        }
        linearLayoutManager = LinearLayoutManager(context)
        rvDownload.layoutManager = linearLayoutManager
        listAdapter = context?.let { BooksDownloadListAdapter(it, packageList,downloadClickContext) }
        rvDownload.adapter = listAdapter

        rvDownload.addOnScrollListener(object :
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
                                freeResource1(MaterialFragment.post_type,MaterialFragment.cat_slug)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        })

    }




     fun downLoadFile(url: String, title: String, position: Int) {
        if (url.isNotEmpty()) {
            val extension = url?.substring(url.lastIndexOf("."))
            val fileName = title + "pdf" + System.currentTimeMillis() + "." + extension
            CHANNEL_DESC = title
            val requestID = System.currentTimeMillis().toInt()
            NOTIFICATION_ID = requestID
            Helpers.startDownloadingFile(null,requireActivity(),url, fileName, "PDF", workManager, requireActivity())
        } else
            Toast.makeText(requireActivity(), "URL not found!", Toast.LENGTH_SHORT).show()
    }

    override fun onDownloadClick(position: String,title:String) {
        downLoadFile(
           position,
           title,
            0
        )
    }

    private fun freeResource1(post_type: String ,cat_slug: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["post_type"] = post_type
        request["cat_slug"] = cat_slug
        request["page"] = ""+pageNo


        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.freeResource(request)
            val response: FreeResourceResponse = result.body()!!
            if (response.status) {

                if (pageNo==0){
                    if (response.list!!.isNotEmpty()){
                        packageList.clear()
                        packageList.addAll(response.list!!)
                        listAdapter!!.notifyDataSetChanged()
                    }

                }else{
                    loading = if (response.list!!.isNotEmpty()){
                        packageList.addAll(response.list!!)
                        listAdapter!!.notifyDataSetChanged()
                        true
                    }else{
                        false;
                    }
                }

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




}
