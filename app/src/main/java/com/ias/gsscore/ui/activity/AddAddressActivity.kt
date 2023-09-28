package com.ias.gsscore.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityAddAddressBinding
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.viewmodel.AddressEditViewModel
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class AddAddressActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddAddressBinding
    lateinit var viewModel: AddressEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_address)
        viewModel = ViewModelProvider(this,ActivityViewModelFactory(binding,application))[AddressEditViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.context = this
        viewModel.addressId = intent.getStringExtra("id").toString()
        if (viewModel.addressId!=""){
            binding.headerTitle.text = "Update Address"
            binding.btnContinue.text = "Update"
            viewModel.addressDetailsApi(viewModel.addressId)
        }else
            binding.headerTitle.text = "Add Address"
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(this)
        viewModel.stateList()

    }


}