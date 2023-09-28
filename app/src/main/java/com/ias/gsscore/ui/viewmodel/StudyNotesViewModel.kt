package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentStudyNotesBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.cart.CartItemListResponse
import com.ias.gsscore.network.response.studynotes.CategoryList
import com.ias.gsscore.network.response.studynotes.ProductList
import com.ias.gsscore.network.response.studynotes.ProductListResponse
import com.ias.gsscore.ui.adapter.StudyNotesCategoryAdapter
import com.ias.gsscore.ui.adapter.StudyNotesProductAdapter
import com.ias.gsscore.utils.SingletonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException

class StudyNotesViewModel(binding: ViewDataBinding) : ViewModel() {
    val binding = binding as FragmentStudyNotesBinding

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    var adapterCategory = StudyNotesCategoryAdapter()
    lateinit var adapterProduct :StudyNotesProductAdapter
    var categoryList: ArrayList<CategoryList> = arrayListOf()
    lateinit var cartResponse: CartItemListResponse
    var offSet = 0
    lateinit var clickListener: StudyNotesCategoryAdapter.ClickListener
    var pageNo = 0
    private var packageList: MutableList<ProductList> = arrayListOf()
    var limit = 10
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0
    private var loading = true
    lateinit var gridLayoutManager:GridLayoutManager
    var m_id=""
    var where_from=""

    fun init(){
        gridLayoutManager=GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.rvStudyList.layoutManager = gridLayoutManager


        adapterProduct = StudyNotesProductAdapter(context, packageList,"")
        binding.rvStudyList.adapter = adapterProduct
        binding.rvStudyList.addOnScrollListener(object :
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
                                apiProductList(where_from,m_id,pageNo)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun apiProductList(whereFrom: String, id: String, offSet: Int) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["offset"] = offSet.toString()
        if (id != "")
            request["product_category"] = id
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.productList(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            val productResponse: ProductListResponse = result.body()!!
            if (productResponse.status) {
                if (pageNo==0){
                    if (productResponse.productList!!.isNotEmpty()){
                        packageList.clear()
                        packageList.addAll(productResponse.productList!!)
                        adapterProduct!!.notifyDataSetChanged()
                    }

                }else{
                    loading = if (productResponse.productList!!.isNotEmpty()){
                        packageList.addAll(productResponse.productList!!)
                        adapterProduct!!.notifyDataSetChanged()
                        true
                    }else{
                        false;
                    }
                }
                if (productResponse.productList.size > 0) {
                   // adapterProduct.update(packageList, "productList")
                    if (whereFrom == "FirstTime") {
                        categoryList=ArrayList()
                        val catList = CategoryList()
                        catList.id = "-1"
                        catList.title = "All"
                        categoryList.add(catList)
                        categoryList.addAll(productResponse.categoryList)
                        adapterCategory.update(categoryList, clickListener)
                    }
                } else
                    Toast.makeText(
                        context,
                        "There is no Product.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
            } else {
                Toast.makeText(
                    context,
                    productResponse.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    fun deliveryItemListApiCall() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["coupon_code"] = ""
        coroutineScope.launch {
            loadingDialog.show()

            val result = apiService.cartItemList(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            cartResponse = result.body()!!
            if (cartResponse.status) {
                SingletonClass.instance.cartCountLayout!!.visibility= View.VISIBLE
                SingletonClass.instance.cartCount!!.text=cartResponse.cartItems!!.totalItems.toString()



            } else {
                Toast.makeText(
                    context,
                    cartResponse.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }
}