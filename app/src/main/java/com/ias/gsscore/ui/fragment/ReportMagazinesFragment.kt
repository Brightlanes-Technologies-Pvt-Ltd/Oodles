package com.ias.gsscore.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentReportMagzinesBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.network.response.freeresource.CurrentAffairResponse
import com.ias.gsscore.network.response.freeresource.FreeResourceList
import com.ias.gsscore.network.response.freeresource.FreeResourceResponse
import com.ias.gsscore.ui.adapter.FreeResourceListAdapter
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.HashMap

class ReportMagazinesFragment(context: Context) : Fragment()  {

    lateinit var binding: FragmentReportMagzinesBinding
    var materialTopList = ArrayList<MaterialResponse>();
    //private var studyMaterialListAdapter: StudyMaterialListAdapter? = null
    lateinit var loadingDialog: AlertDialog
    private var freeResourceListAdapter: FreeResourceListAdapter? = null
    var pageNo = 0
    var limit = 10
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var loading = true
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var packageList: MutableList<FreeResourceList> = arrayListOf()



    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_magzines, container, false)
        loadingDialog = RetrofitHelper.loadingDialog(requireContext())
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(Preferences.getInstance(context).frTitle==""){

            freeResource1(
                MaterialFragment.post_type, MaterialFragment.cat_slug)
        }else {
            var arr1 = Preferences.getInstance(context).frTitle.split(";")
            if (arr1[1] == "false") {
                freeResource1(MaterialFragment.post_type, MaterialFragment.cat_slug)
            } else {
                currentAffair1(MaterialFragment.post_type, MaterialFragment.start, MaterialFragment.end)
            }
        }
        var gridLayoutManager = GridLayoutManager(context, 2)
        binding.rvResourceList.layoutManager = gridLayoutManager
        freeResourceListAdapter = context?.let { FreeResourceListAdapter(it,packageList) }
        binding.rvResourceList.adapter = freeResourceListAdapter
        binding.rvResourceList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.childCount
                    totalItemCount = gridLayoutManager.itemCount
                    pastVisiblesItems = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            try {
                                loading = false
                                pageNo += 1
                                if(Preferences.getInstance(context).frTitle==""){

                                    freeResource1(
                                        MaterialFragment.post_type, MaterialFragment.cat_slug)
                                }else {
                                    var arr1 = Preferences.getInstance(context).frTitle.split(";")
                                    if (arr1[1] == "false") {
                                        freeResource1(MaterialFragment.post_type, MaterialFragment.cat_slug)
                                    } else {
                                        currentAffair1(MaterialFragment.post_type, MaterialFragment.start, MaterialFragment.end)
                                    }
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        })
        //setMaterialAdapter()
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
                        freeResourceListAdapter!!.notifyDataSetChanged()
                    }

                }else{
                    loading = if (response.list!!.isNotEmpty()){
                        packageList.addAll(response.list!!)
                        freeResourceListAdapter!!.notifyDataSetChanged()
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

    private fun currentAffair1(post_type: String ,start: String,end:String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["post_type"] = post_type
        request["from_date"] = start
        request["to_date"] = end
        request["page"] = ""+pageNo



        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.currentAffair(request)
            val response: CurrentAffairResponse = result.body()!!
            if (response.status) {

                if (pageNo==0){
                    if (response.list!!.isNotEmpty()){
                        packageList.clear()
                        packageList.addAll(response.list!!)
                        freeResourceListAdapter!!.notifyDataSetChanged()
                    }

                }else{
                    loading = if (response.list!!.isNotEmpty()){
                        packageList.addAll(response.list!!)
                        freeResourceListAdapter!!.notifyDataSetChanged()
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
