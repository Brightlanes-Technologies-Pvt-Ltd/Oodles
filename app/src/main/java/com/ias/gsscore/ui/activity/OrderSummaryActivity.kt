package com.ias.gsscore.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityOrderSummaryBinding
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.adapter.AddressListAdapter
import com.ias.gsscore.ui.viewmodel.OrderSummaryViewModel
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class OrderSummaryActivity : AppCompatActivity(), AddressListAdapter.OnClickAddressInterface {
    lateinit var binding: ActivityOrderSummaryBinding
    lateinit var viewModel: OrderSummaryViewModel
    lateinit var amount: String
    lateinit var gst: String
    var fromcart: Boolean? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_summary)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[OrderSummaryViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.context = this
        amount = intent.getStringExtra("amount").toString()
        gst = intent.getStringExtra("gst").toString()
       viewModel.count = intent.getStringExtra("count").toString()
        fromcart = intent.getBooleanExtra("fromcart", false);
        viewModel.fromCart = fromcart!!
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(this)
        binding.headerTitle.text = "Order Summary"
        viewModel.amount = amount
        binding.totItems.setText("Price Details (${viewModel.count} Items)")
        viewModel.gst = gst
        binding.tvTotalMrp.text = "₹$amount"
        if (!fromcart!!) {
            if (gst.toFloat() > 0) {
                binding.gst.text = "GST(${gst})%"
                binding.gstAmount.text = "₹" + ((amount.toFloat() * gst.toFloat()) / 100).toString()
                binding.tvTotalAmount.text =
                    "₹" + (amount.toFloat() + ((amount.toFloat() * gst.toFloat()) / 100))
                binding.tvFinalAmount.text =
                    "₹" + (amount.toFloat() + ((amount.toFloat() * gst.toFloat()) / 100))
            } else {
                binding.gst.text = "gst included"
                binding.gstAmount.text = "₹0"
                binding.tvTotalAmount.text =
                    "₹" + (amount.toFloat())
                binding.tvFinalAmount.text =
                    "₹" + (amount.toFloat())
            }
        }else{
            binding.gst.text = "gst included"
            binding.gstAmount.text = "₹0"
            binding.gst.text = "gst included"
            binding.gstAmount.text = "₹0"
            binding.tvTotalAmount.text =
                "₹" + (amount.toFloat())
            binding.tvFinalAmount.text =
                "₹" + (amount.toFloat())
        }


        binding.addAddress.setOnClickListener {
            val intent = Intent(this, AddAddressActivity::class.java)
            intent.putExtra("id", "")
            startActivity(intent)
        }

        binding.viewItems.setOnClickListener{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.addressListApi()
        if (intent.getStringExtra("flag").equals("CourseDetail")) {
            viewModel.courseDetailApi()
        } else {
            viewModel.cartProductsApi()
        }

    }

    override fun onClick(id: String, position: Int, whereFrom: String) {
        when (whereFrom) {
            "RemoveAddress" -> viewModel.deleteAddress(id)
            "EditAddress" -> viewModel.editAddress(id)
            else -> {
                //RadioButton Default Address
                viewModel.funDefaultAddressSet(position)
            }
        }
    }
}