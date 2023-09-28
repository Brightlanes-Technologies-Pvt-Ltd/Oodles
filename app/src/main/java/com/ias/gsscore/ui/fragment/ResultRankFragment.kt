package com.ias.gsscore.ui.fragment

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
import com.ias.gsscore.databinding.FragmentResultRankBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.myaccount.RankList
import com.ias.gsscore.network.response.testresult.TestResultResponse
import com.ias.gsscore.ui.adapter.RankListAdapter
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ResultRankFragment(private val testId: String, private val programId: String, private val resultId: String) : Fragment() {
    lateinit var binding: FragmentResultRankBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: RankListAdapter? = null
    private val viewModelJob = Job()
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private lateinit var rankList: ArrayList<RankList>
    var pageNo = 1
    var limit = 10
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var loading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_result_rank,
            container,
            false
        )
        initialData()
        return binding.root
    }

    private fun initialData() {
        loadingDialog = RetrofitHelper.loadingDialog(requireContext())
        prelimsLeaderboardApiCall()
    }


    private fun setAdapter(rankList: ArrayList<RankList>) {
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = RankListAdapter(rankList)
        binding.recyclerView.adapter = adapter


        binding.recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                  /*  visibleItemCount = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisiblesItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            val jsonObject = JSONObject()
                            try {
                                loading = false
                                pageNo += 1
                                prelimsLeaderboardApiCall()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }*/
                }
            }
        })


    }

    private fun prelimsLeaderboardApiCall() {
        var request: HashMap<String, String> = HashMap()
        request["userId"] = Preferences.getInstance(context).userId
        request["testId"] = testId
        request["programId"] = programId
        request["limit"] = "100"
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.prelimsLeaderboard(request)
            val response: TestResultResponse = result.body()!!
            if (response.status) {
                if (response.rankList.size>0){
                    rankList = response.rankList
                    setAdapter(rankList)
                   /* if (pageNo == 1) {
                      //  rankList.clear()
                        rankList = response.rankList
                        setAdapter(rankList)
                       // adapter!!.updateList(rankList)

                    } else {
                        loading = if (response.rankList.size > 0) {
                            rankList.addAll(response.rankList)
                           // adapter!!.updateList(rankList)
                            true
                        } else {
                            false
                        }
                    }*/
                }
                else
                    Toast.makeText(
                        context,
                        "Data not found",
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

}