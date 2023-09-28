package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.network.response.address.AddressList
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityAddAddressBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.address.AddressResponse
import com.ias.gsscore.network.response.myaccount.StateListResponse
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddressEditViewModel(binding: ViewDataBinding) : ViewModel() {
    val binding = binding as ActivityAddAddressBinding
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
        lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var address: ArrayList<AddressList> = arrayListOf()
    var addressId = ""
    var stateId = ""
    var response: StateListResponse?=null
    fun backPress() {
        (context as Activity).onBackPressed()
    }

    fun onClickContinue() {
        if (checkValidation())
            addUpdateAddressApi()
    }

    private fun checkValidation(): Boolean {
        if (binding.etName.text!!.isEmpty()) {
            showToast("Enter Name")
            return false
        }
        if (binding.etMobile.text!!.isEmpty()) {
            showToast("Enter mobile number")
            return false
        }
        if (binding.etMobile.text!!.length != 10) {
            showToast("Enter valid mobile number")
            return false
        }
        if (binding.etAddress.text!!.isEmpty()) {
            showToast("Enter address")
            return false
        }
        if (binding.etPinCode.text!!.isEmpty()) {
            showToast("Enter pin code")
            return false
        }
        if (binding.etLocality.text!!.isEmpty()) {
            showToast("Enter locality/town")
            return false
        }
        if (binding.etState.selectedItem.toString()=="Select State") {
            showToast("Enter state")
            return false
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    fun addUpdateAddressApi() {
        var request = funRequestCreate()
        coroutineScope.launch {
            loadingDialog.show()
            var result = if (addressId == "")
                apiService.addAddress(request)
            else
                apiService.updateAddress(request)
            if (result.body() == null) {
                showToast(context.getString(R.string.error_msg))
                loadingDialog.dismiss()
                return@launch
            }
            val response: AddressResponse = result.body()!!
            if (response.status) {
               showToast(response.message)
                backPress()
            } else {
                showToast(response.error)
            }
            loadingDialog.dismiss()
        }
    }

        fun stateList() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.stateList(request)
            if (result.body() == null) {
                showToast(context.getString(R.string.error_msg))
                loadingDialog.dismiss()
                return@launch
            }
           response= result.body()!!
            if (response!!.status) {
                var spinnerList: Array<String?> = arrayOfNulls(response!!.state_list.size + 1)
                spinnerList[0] = "Select State"
                for (i in response!!.state_list.indices) {
                    spinnerList[i + 1] = response!!.state_list[i].state_name
                }
                val adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_dropdown_item, spinnerList
                )
                binding.etState.adapter = adapter

                binding.etState.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    @SuppressLint("SetTextI18n")
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                    ) {

                        stateId = if (position == 0)
                            ""
                        else
                            response!!.state_list[position-1].id!!
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            } else {
                showToast(response!!.error)
            }
            loadingDialog.dismiss()
        }
    }

    private fun funRequestCreate(): java.util.HashMap<String, String> {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["id"] = addressId
        request["fullname"] = binding.etName.text.toString()
        request["landmark"] = ""
        request["mobile"] = binding.etMobile.text.toString()
        request["state"] = stateId
        request["city"] = binding.etLocality.text.toString()
        request["address"] = binding.etAddress.text.toString()
        request["zip_code"] = binding.etPinCode.text.toString()
        if (binding.checkBoxMark.isChecked)
            request["default"] = "1"
        else
            request["default"] = "0"

        return request
    }

    fun addressDetailsApi(addressId: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["id"] = addressId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.addressDetails(request)
            if (result.body() == null) {
                showToast(context.getString(R.string.error_msg))
                loadingDialog.dismiss()
                return@launch
            }
            val response: AddressResponse = result.body()!!
            if (response.status) {
                funAddressDetailsSet(response.addressDetails)
            } else {
               showToast(response.error)
            }
            loadingDialog.dismiss()
        }
    }

    private fun funAddressDetailsSet(data: AddressList) {
        binding.etName.setText(data.fullname)
        binding.etMobile.setText(data.mobile)
        for(i in response!!.state_list.indices){
            if(response!!.state_list[i].state_name==data.stateName){
                binding.etState.setSelection(i)
                break
            }
        }

        binding.etLocality.setText(data.city)
        binding.etAddress.setText(data.address)
        binding.etPinCode.setText(data.zipCode)
        binding.checkBoxMark.isChecked = data.defaultAddress=="1"
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        )
            .show()
    }

}