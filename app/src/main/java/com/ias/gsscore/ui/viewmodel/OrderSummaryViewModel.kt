package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.ias.gsscore.network.response.address.AddressList
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityOrderSummaryBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.address.AddressResponse
import com.ias.gsscore.network.response.cart.CartItemListResponse
import com.ias.gsscore.network.response.cart.CartList
import com.ias.gsscore.network.response.coursedetails.CourseDetailResponse
import com.ias.gsscore.ui.activity.AddAddressActivity
import com.ias.gsscore.ui.activity.ConfirmOrderActivity
import com.ias.gsscore.ui.adapter.AddressListAdapter
import com.ias.gsscore.ui.adapter.ProductDetailAdapter
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderSummaryViewModel(binding: ViewDataBinding) : ViewModel() {
    lateinit var amount: String
    lateinit var gst: String
    var count=""

    val binding = binding as ActivityOrderSummaryBinding
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    var adapter = AddressListAdapter()
    var courseAdapter = ProductDetailAdapter()
    lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var addressList : ArrayList<AddressList> = arrayListOf()
    private var courseList : ArrayList<CartList> = arrayListOf()
    private var selectedAddress:AddressList?=null
    var fromCart:Boolean=false
    fun backPress() {
        (context as Activity).onBackPressed()
    }

    @SuppressLint("SetTextI18n")
    fun courseDetailApi() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["coupon_code"] = ""
        coroutineScope.launch {
            val result = apiService.courseDetailList(request)
            Log.d("*** OrderSummaryVM ProductDetails result >>", ""+result)

                if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@launch
            }
            val response: CourseDetailResponse = result.body()!!

            if (response.status!!) {
                courseList = response.cartItems?.courseList ?: arrayListOf()
                if(response.cartItems?.courseList?.size!! > 0){
                    courseAdapter.update(courseList,context, "Course")
                }
            } else {
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    fun cartProductsApi() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["coupon_code"] = ""
        coroutineScope.launch {
            val result = apiService.cartItemList(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@launch
            }
            val response: CartItemListResponse = result.body()!!

            if (response.status!!) {
                courseList = response.cartItems?.list ?: arrayListOf()
                if(response.cartItems?.list?.size!! > 0){
                    courseAdapter.update(courseList,context, "Study Notes")
                }
            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun addressListApi() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["coupon_code"] = ""
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.addressList(request)
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
            val response: AddressResponse = result.body()!!
            if (response.status) {
                addressList = response.addressList
                if(response.addressList.size==1){
                funDefaultAddressSet(0)
                }else if(response.addressList.size==0){
                    selectedAddress=null
                }
                adapter.update(addressList,context)
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

    fun deleteAddress(id: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["id"] = id
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.deleteAddress(request)
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
            val response: AddressResponse = result.body()!!
            if (response.status) {
                addressListApi()
                Toast.makeText(
                    context,
                    response.error,
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

    fun btnContinue(){
        if (selectedAddress==null){
            Toast.makeText(context,"Select Address",Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(context, ConfirmOrderActivity::class.java)
        val gson = Gson()
        intent.putExtra("amount",amount)
        intent.putExtra("gst",gst)
        intent.putExtra("count",count)
        intent.putExtra("address",gson.toJson(selectedAddress))
        intent.putExtra("fromcart",fromCart)
        context.startActivity(intent)
    }

    fun editAddress(id: String) {
        val intent = Intent(context, AddAddressActivity::class.java)
        intent.putExtra("id",id)
        context.startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun funDefaultAddressSet(position: Int) {
        for (i in addressList.indices){
            addressList[i].defaultAddress="0"
        }
        addressList[position].defaultAddress = "1"
        selectedAddress = addressList[position]
        adapter.notifyDataSetChanged()
    }

}