package com.ias.gsscore.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentMyAccountBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.home.LatestCourses
import com.ias.gsscore.network.response.myaccount.MyCourseListResponse
import com.ias.gsscore.network.response.myaccount.PackageList
import com.ias.gsscore.ui.adapter.CourseFragmentListAdapter
import com.ias.gsscore.ui.adapter.LatestCourseListAdapter
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException

class MyAccountFragment : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    lateinit var loadingDialog: AlertDialog
    lateinit var binding: FragmentMyAccountBinding
    private var courseListAdapter: CourseFragmentListAdapter? = null
    var adapterLatestCourse = LatestCourseListAdapter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManagerLatestCourse: LinearLayoutManager
    private var packageList: MutableList<PackageList> = arrayListOf()
    var latestCourseList: ArrayList<LatestCourses> = arrayListOf()
    var pageNo = 0
    var limit = 10
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var loading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = RetrofitHelper.loadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_account, container, false)
        initialData()
        return binding.root
    }

    private fun initialData() {
        courseListApi()
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvCourseList.layoutManager = linearLayoutManager
        courseListAdapter =
            context?.let { CourseFragmentListAdapter(it, packageList) }
        binding.rvCourseList.adapter = courseListAdapter

        linearLayoutManagerLatestCourse = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvLatestCourseList.layoutManager = linearLayoutManagerLatestCourse
        adapterLatestCourse = LatestCourseListAdapter()
        binding.rvLatestCourseList.adapter = adapterLatestCourse

        binding.rvCourseList.addOnScrollListener(object :
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
                                courseListApi()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun courseListApi() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        //  request["user_type"] = Preferences.getInstance(context).userType
        request["offset"] = ""+pageNo
//        request["user_id"] = "170885"
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.myPackagesList(request)
            val response: MyCourseListResponse = result.body()!!
            if (response.status) {
                if (pageNo==0){
                    if (response.package_list!!.isNotEmpty()){
                        packageList.clear()
                        packageList.addAll(response.package_list!!)
                        courseListAdapter!!.notifyDataSetChanged()
                    }
                    latestCourseList = response.latest_courses!!
                    adapterLatestCourse.update(latestCourseList,latestCourseList.size,requireActivity())
                }else{
                    loading = if (response.package_list!!.isNotEmpty()){
                        packageList.addAll(response.package_list!!)
                        courseListAdapter!!.notifyDataSetChanged()
                        true
                    }else{
                        false;
                    }
                }
                if (packageList.isNotEmpty()) {
                    binding.tvDataNotFound.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                } else {
                    binding.tvDataNotFound.text = response.message
                    binding.tvDataNotFound.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.GONE
                }
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

}