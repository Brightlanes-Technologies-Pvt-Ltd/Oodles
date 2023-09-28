package com.ias.gsscore.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityCartItemListBinding
import com.ias.gsscore.ui.adapter.CartDeliveryItemAdapter
import com.ias.gsscore.ui.viewmodel.CartItemListViewModel
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.adapter.CartWishlistItemAdapter

class CartItemListActivity : AppCompatActivity(), CartDeliveryItemAdapter.RemoveCartItemInterface,
    CartWishlistItemAdapter.RemoveWishlistItemInterface {
    lateinit var binding: ActivityCartItemListBinding
    lateinit var viewModel: CartItemListViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart_item_list)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[CartItemListViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.context = this
        viewModel.removeCartItemInterface = this
        viewModel.removeWishlistItemInterface = this
        val whereFrom = intent.getStringExtra("whereFrom")
        if (whereFrom == "cart") {
            viewModel.whereFrom = 0
            binding.headerTitle.text = "Shopping Cart"
            binding.orderLayout.visibility = View.VISIBLE
        } else {
            binding.headerTitle.text = "Wish List"
            viewModel.whereFrom = 1
            binding.orderLayout.visibility = View.GONE
        }

        viewModel.loadingDialog = RetrofitHelper.loadingDialog(this)
        viewModel.deliveryItemListApiCall()

        binding.btnContinue.setOnClickListener {
            val intent =Intent(this,OrderSummaryActivity::class.java)
            intent.putExtra("amount",binding.total.text.toString().replace("₹",""))
            intent.putExtra("count",viewModel.size.toString())
            intent.putExtra("flag","CartItemListActivity")
            intent.putExtra("gst","")
            if(whereFrom == "cart") {
                intent.putExtra("fromcart",true )
            }
            startActivity(intent)
        }
    }

    override fun removeCartItem(productId: String, position: Int) {
        viewModel.removeCartItem(productId, position)
    }

    override fun removeWishlistItem(productId: String, position: Int) {
        viewModel.removeWishlistItem(productId, position)
    }
}