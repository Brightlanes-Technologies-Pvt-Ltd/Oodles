package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityCartItemListBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.response.cart.CartItemResponse
import com.ias.gsscore.ui.adapter.CartDeliveryItemAdapter
import com.ias.gsscore.ui.adapter.CartDigitalItemAdapter
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.cart.CartItemListResponse
import com.ias.gsscore.ui.adapter.CartWishlistItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CartItemListViewModel(binding: ViewDataBinding) : ViewModel() {
    val binding = binding as ActivityCartItemListBinding
    lateinit var cartResponse: CartItemListResponse
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    var adapterCart = CartDeliveryItemAdapter()
    var adapterWishlist = CartWishlistItemAdapter()
    var cartDigitalItemAdapter = CartDigitalItemAdapter()
    lateinit var removeCartItemInterface: CartDeliveryItemAdapter.RemoveCartItemInterface
    lateinit var removeWishlistItemInterface: CartWishlistItemAdapter.RemoveWishlistItemInterface
    var whereFrom = 0
    var size=0;
    fun backPress() {
        (context as Activity).onBackPressed()
    }

    @SuppressLint("SetTextI18n")
    fun deliveryItemListApiCall() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["coupon_code"] = ""
        coroutineScope.launch {
            loadingDialog.show()
            if (whereFrom==0){
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
                cartResponse =  result.body()!!
            } else{
                val result = apiService.wishlistItemList(request)
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
            }
            if (cartResponse.status) {
                if (whereFrom==0)
                    funCartDataSet()
                else
                    funWishlistDataSet()
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

    @SuppressLint("SetTextI18n")
    private fun funWishlistDataSet() {
        binding.orderLayout.visibility = View.GONE
        binding.tvTotalItem.text = "Delivery items (${cartResponse.wishlistItems.size})"
        if (cartResponse.wishlistItems.size > 0)
            adapterWishlist.update(cartResponse.wishlistItems, removeWishlistItemInterface)
        else
            Toast.makeText(
                context,
                "There is no Product in the Cart.",
                Toast.LENGTH_SHORT
            )
                .show()
    }

    @SuppressLint("SetTextI18n")
    private fun funCartDataSet() {
        binding.orderLayout.visibility = View.VISIBLE
        binding.total.text = "₹ ${cartResponse.cartItems!!.totalPayableAmount}"
        size=cartResponse.cartItems!!.list.size
        binding.tvTotalItem.text = "Delivery items (${cartResponse.cartItems!!.list.size})"
        if (cartResponse.cartItems != null && cartResponse.cartItems!!.list.size > 0)
            adapterCart.update(cartResponse.cartItems!!.list, removeCartItemInterface)
        else
            adapterCart.update(cartResponse.cartItems!!.list, removeCartItemInterface)

    }

    fun removeCartItem(productId: String,position:Int) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["product_id"] = productId
        request["mode"] = ""
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.removeCartItem(request)
            val response: CartItemResponse = result.body()!!
            if (response.status) {
                loadingDialog.dismiss()
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()
               // adapter.removeAndUpdateList(position)
                deliveryItemListApiCall()
            } else {
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    fun removeWishlistItem(productId: String,position:Int) {
      /*  var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["product_id"] = productId
        request["mode"] = ""*/
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.removeWishItem( Preferences.getInstance(context).userId,productId,"1")
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
            val response: CartItemResponse = result.body()!!
            if (response.status) {
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()
                // adapter.removeAndUpdateList(position)
                deliveryItemListApiCall()
            } else {
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }
}