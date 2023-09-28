package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentStudyNotesDetailsBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.cart.CartItemListResponse
import com.ias.gsscore.network.response.cart.CartItemResponse
import com.ias.gsscore.network.response.studynotes.ProductDetailsResponse
import com.ias.gsscore.ui.adapter.StudyNotesDetailsPagerAdapter
import com.ias.gsscore.ui.adapter.StudyNotesProductDetAdapter
import com.ias.gsscore.utils.Helpers
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.utils.SingletonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StudyNotesDetailsViewModel(binding: ViewDataBinding) : ViewModel() {
    val binding = binding as FragmentStudyNotesDetailsBinding
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    private var adapter: StudyNotesDetailsPagerAdapter? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    var productId = ""
    lateinit var cartResponse: CartItemListResponse
    var adapterProduct = StudyNotesProductDetAdapter()



    @SuppressLint("SetTextI18n")
    fun apiProductDetails() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["product_id"] = productId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.productDetails(request)
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
            val productResponse: ProductDetailsResponse = result.body()!!
            if (productResponse.status) {
                setProductDetails(productResponse)
                deliveryItemListApiCall()
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

    @SuppressLint("SetTextI18n")
    private fun setProductDetails(productResponse: ProductDetailsResponse) {
        binding.title.text = productResponse.productsInfo!!.productDetails!!.title
        Helpers.setWebViewText(binding.tvDescription,productResponse.productsInfo!!.productDetails!!.highlights!!)
       // binding.tvDescription.text = productResponse.productsInfo!!.productDetails!!.highlights
        binding.discountedPrice.text = "₹${productResponse.productsInfo!!.productDetails!!.amount}"
        if (productResponse.productsInfo!!.relatedProducts.size > 0) {
            adapterProduct.update(productResponse.productsInfo!!.relatedProducts,"productDetails")
        }
        var img_arr=ArrayList<String>()
        for(i in 0 until 4){
            img_arr.add(productResponse.productsInfo!!.productDetails!!.imageUrl!!)
        }
        adapter = StudyNotesDetailsPagerAdapter(context,img_arr)
        binding.viewPager2.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPager2)
    }


    fun addToCartApiCall() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["product_id"] = productId
        request["mode"] = "1"
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.addToCart(request)
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
                loadingDialog.dismiss()
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                deliveryItemListApiCall()


            }else{
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

    fun clickAddToWishlist(){
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["product_id"] = productId
        request["mode"] = "1"
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.addToWishlist(request)
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
                loadingDialog.dismiss()
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                deliveryItemListApiCall()
            }else{
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