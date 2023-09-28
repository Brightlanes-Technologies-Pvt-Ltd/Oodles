package com.ias.gsscore.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.ias.gsscore.R
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.freeresource.FreeResourceList
import com.ias.gsscore.network.response.freeresource.FreeResourceResponse
import com.ias.gsscore.ui.adapter.PrelimsListAdapter
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.HashMap

class PrelimsFragment(context: Context) : Fragment() {

    @BindView(R.id.rvMaterial)
    lateinit var rvMaterial: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var materialListAdapter: PrelimsListAdapter? = null
    var pageNo = 0
    var limit = 10
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var loading = true
    private var packageList: MutableList<FreeResourceList> = arrayListOf()

    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.frament_prelims, container, false)
        ButterKnife.bind(this, view);
        loadingDialog = RetrofitHelper.loadingDialog(requireContext())
        return view;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setMaterialAdapter()
    }

    private fun setMaterialAdapter() {
        freeResource1(MaterialFragment.post_type,MaterialFragment.cat_slug)
        linearLayoutManager = LinearLayoutManager(context)
        rvMaterial.layoutManager = linearLayoutManager
        materialListAdapter = context?.let { PrelimsListAdapter(it, packageList, coroutineScope,apiService,loadingDialog) }
        rvMaterial.adapter = materialListAdapter

        rvMaterial.addOnScrollListener(object :
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
                        materialListAdapter!!.notifyDataSetChanged()
                    }

                }else{
                    loading = if (response.list!!.isNotEmpty()){
                        packageList.addAll(response.list!!)
                        materialListAdapter!!.notifyDataSetChanged()
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
